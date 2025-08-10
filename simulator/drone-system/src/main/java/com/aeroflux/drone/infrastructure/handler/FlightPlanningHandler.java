package com.aeroflux.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aeroflux.drone.domain.handler.StepHandler;
import com.aeroflux.drone.domain.model.Authorization;
import com.aeroflux.drone.domain.model.DataStatus;
import com.aeroflux.drone.domain.model.DroneContext;
import com.aeroflux.drone.domain.model.DroneFlightMode;
import com.aeroflux.drone.domain.model.GeoZone;
import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.domain.model.NearbyDroneStatus;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.model.RainCell;
import com.aeroflux.drone.domain.model.SupportPoint;
import com.aeroflux.drone.domain.navigation.GeoCalculator;
import com.aeroflux.drone.infrastructure.controller.FlightController;
import com.aeroflux.drone.infrastructure.service.DroneServiceFacade;
import com.aeroflux.drone.infrastructure.service.authorization.AuthorizationService;
import com.aeroflux.drone.infrastructure.service.geozone.GeoZoneService;
import com.aeroflux.drone.infrastructure.service.log.LogService;
import com.aeroflux.drone.infrastructure.service.navigation.DroneSafetyNavigationService;
import com.aeroflux.drone.infrastructure.service.navigation.NavigationService;
import com.aeroflux.drone.infrastructure.service.supportPoint.SupportPointService;
import com.aeroflux.drone.infrastructure.service.weather.WeatherService;

public class FlightPlanningHandler implements StepHandler {

    private final DroneContext context;
    private final NavigationService navigationService;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final AuthorizationService authorizationService;
    private final DroneSafetyNavigationService droneSafetyNavigationService;
    private final SupportPointService supportPointService;
    private final FlightController flightController;
    private final GeoCalculator geoCalculator;
    private final LogService logService;

    private List<GeoZone> lastGeoZonesConsidered = new ArrayList<>();
    private List<RainCell> lastRainCellsConsidered = new ArrayList<>();
    private List<NearbyDroneStatus> lastConflictingDronesConsidered = new ArrayList<>();

    public FlightPlanningHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.navigationService = droneServices.getNavigationService();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.droneSafetyNavigationService = droneServices.getDroneSafetyNavigationService();
        this.supportPointService = droneServices.getSupportPointService();
        this.flightController = droneServices.getFlightController();
        this.logService = droneServices.getLogService();
        this.geoCalculator = new GeoCalculator();
    }

    @Override
    public boolean handle() {

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING_REQUEST) {
            Position landingPosition = navigationService.configureVerticalLanding(flightController.getCurrentPosition());
            context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING);
            logService.info(LogConstants.Component.FLIGHT_PLANNING_HANDLER, LogConstants.Event.EMERGENCY_LANDING,
                            "Emergency landing at " + landingPosition);
            return false;
        }

        boolean envDataAvailable = geoZoneService.getGeoZonesStatus() == DataStatus.AVAILABLE &&
            weatherService.getRainCellsStatus() == DataStatus.AVAILABLE &&
            authorizationService.getAuthorizationsStatus() == DataStatus.AVAILABLE &&
            supportPointService.getSupportPointsStatus() == DataStatus.AVAILABLE;

        if (!envDataAvailable) return false;

        Map<String, GeoZone> geoZones = geoZoneService.getGeoZones();
        Map<String, Authorization> authorizations = authorizationService.getAuthorizations();

        List<GeoZone> geoZonesToConsider = new ArrayList<>();
        for (Entry<String, GeoZone> entry : geoZones.entrySet()) {
            String geoZoneId = entry.getKey();
            GeoZone geoZone = entry.getValue();
            if (geoZone.isActive()) {
                Authorization auth = authorizations.get(geoZoneId);
                if (auth == null || !auth.isGranted()) {
                    geoZonesToConsider.add(geoZone);
                }
            }
        }
        List<RainCell> rainCellsToConsider = weatherService.getRainCells();
        List<NearbyDroneStatus> conflictingDrones = droneSafetyNavigationService.getConflictingDrones();
      
        Position actualDestination = navigationService.getCurrentDestination();

        if (context.getFlightMode() == DroneFlightMode.LANDING_REQUEST || context.getFlightMode() == DroneFlightMode.ALTERNATIVE_DESTINATION_REQUEST) {

            DataStatus supportPointStatus = supportPointService.getSupportPointsStatus();

            if(supportPointStatus != DataStatus.AVAILABLE) return false;
            
            List<SupportPoint> supportPoints = new ArrayList<>();
            supportPoints.add(new SupportPoint("Destination Point", "Destination Point", context.getDroneProperties().getDestination()));
            supportPoints.add(new SupportPoint("Source Point", "Source Point", context.getDroneProperties().getSource()));
            supportPoints.addAll(supportPointService.getSupportPoints());

            List<Object> zones = new ArrayList<>();
            zones.addAll(geoZonesToConsider);
            zones.addAll(rainCellsToConsider);  

            Iterator<SupportPoint> iterator = supportPoints.iterator();
            while (iterator.hasNext()) {
                SupportPoint supportPoint = iterator.next();
                if (geoCalculator.isPointInZone(new Position(supportPoint.getCoordinate(), 0), zones)) {
                    iterator.remove();
                }
            }

            SupportPoint selectedSupportPoint;
            
            if(context.getFlightMode() == DroneFlightMode.LANDING_REQUEST) {
                selectedSupportPoint = supportPointService.getClosestSupportPoint(flightController.getCurrentPosition(), supportPoints);
            } else {
                selectedSupportPoint = supportPointService.getClosestSupportPoint(actualDestination, supportPoints);
            }
            
            context.setFlightMode(DroneFlightMode.REROUTE_FLIGHT);

            if (selectedSupportPoint == null) {
                Position landingPosition = navigationService.configureVerticalLanding(flightController.getCurrentPosition());
                logService.info(LogConstants.Component.FLIGHT_PLANNING_HANDLER, LogConstants.Event.DESTINATION_UNREACHABLE,
                            "No available Support Point found: landing at " + landingPosition);
                return false;
            }
            logService.info(LogConstants.Component.FLIGHT_PLANNING_HANDLER, LogConstants.Event.REROUTE_FLIGHT,
                            "Rerouting flight to closest support point: " + selectedSupportPoint.getName());

            actualDestination = new Position(selectedSupportPoint.getCoordinate(), 0);
        }

        DataStatus flightPlanStatus = navigationService.getFlightPlanStatus();
        Position lastDestinationRequested = navigationService.getCurrentDestination();

        boolean needToAdaptFlightPlan = 
            !(lastGeoZonesConsidered.equals(geoZonesToConsider) &&
            lastRainCellsConsidered.equals(rainCellsToConsider) &&
            lastDestinationRequested.equals(actualDestination) &&
            (conflictingDrones.isEmpty() || lastConflictingDronesConsidered.equals(conflictingDrones)) );

        if (flightPlanStatus == DataStatus.NOT_REQUESTED ||
            flightPlanStatus == DataStatus.FAILED ||
            needToAdaptFlightPlan) {

            navigationService.generateFlightPlan(
                flightController.getCurrentPosition(),
                actualDestination,
                geoZonesToConsider,
                rainCellsToConsider,
                conflictingDrones
            );
            lastGeoZonesConsidered = geoZonesToConsider;
            lastRainCellsConsidered = rainCellsToConsider;
            lastConflictingDronesConsidered = conflictingDrones;
        }
        return false;
                
    }
        
}



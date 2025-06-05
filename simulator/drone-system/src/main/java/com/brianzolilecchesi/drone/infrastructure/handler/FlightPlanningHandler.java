package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.Authorization;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class FlightPlanningHandler implements StepHandler {

    private final DroneContext context;
    private final NavigationService navigationService;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final AuthorizationService authorizationService;
    private final FlightController flightController;

    private List<GeoZone> geoZonesInFlightPlan = new ArrayList<>();
    private List<RainCell> rainCellsInFlightPlan = new ArrayList<>();

    public FlightPlanningHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.navigationService = droneServices.getNavigationService();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.flightController = droneServices.getFlightController();
    }

    @Override
    public boolean handle() {

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        DataStatus status = navigationService.getFlightPlanStatus();

        if (status == DataStatus.LOADING) {
            if (flightController.isOnGround())
                return true; 
            else 
                return false;
        }

        boolean dataAvailable = geoZoneService.getGeoZonesStatus() == DataStatus.AVAILABLE &&
            weatherService.getRainCellsStatus() == DataStatus.AVAILABLE &&
            authorizationService.getAuthorizationsStatus() == DataStatus.AVAILABLE;

        if (dataAvailable) {
        
            Map<String, GeoZone> geoZones = geoZoneService.getGeoZones();
            Map<String, Authorization> authorizations = authorizationService.getAuthorizations();

            System.out.println("Authori: " + authorizations.values());

            List<GeoZone> geoZonesToConsider = new ArrayList<>();
            for (Entry<String, GeoZone> entry : geoZones.entrySet()) {
                String geoZoneId = entry.getKey();
                GeoZone geoZone = entry.getValue();
                if (geoZone.isActive()) {
                    Authorization auth = authorizations.get(geoZoneId);
                    if (auth == null || !auth.isGranted()) {
                        geoZonesToConsider.add(geoZone);
                    } else {
                        System.out.println("GeoZone " + geoZone.getName() + " is authorized, skipping.");
                    }
                }
            }
            List<RainCell> rainCellsToConsider = weatherService.getRainCells();

            if (status == DataStatus.AVAILABLE) {
                boolean needToAdaptFlightPlan = 
                    !(geoZonesInFlightPlan.equals(geoZonesToConsider) &&
                    rainCellsInFlightPlan.equals(rainCellsToConsider));
                    
                if (needToAdaptFlightPlan) {
                    navigationService.adaptFlightPlan(
                        context.getCurrentDestination(),
                        geoZonesToConsider,
                        rainCellsToConsider
                    );
                    geoZonesInFlightPlan = geoZonesToConsider;
                    rainCellsInFlightPlan = rainCellsToConsider;
                }
                return false;
            }
            
            if (status == DataStatus.NOT_REQUESTED) {
                navigationService.generateFlightPlan(
                    new Position(context.getDroneProperties().getSource(), 0),
                    context.getCurrentDestination(),
                    geoZonesToConsider,
                    rainCellsToConsider
                );
                geoZonesInFlightPlan = geoZonesToConsider;
                rainCellsInFlightPlan = rainCellsToConsider;
            }
        }
        return true;
    }
    
}



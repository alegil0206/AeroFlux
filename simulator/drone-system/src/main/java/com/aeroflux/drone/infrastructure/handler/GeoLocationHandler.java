package com.aeroflux.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.model.RainCell;
import com.aeroflux.drone.domain.navigation.GeoCalculator;
import com.aeroflux.drone.infrastructure.controller.FlightController;
import com.aeroflux.drone.infrastructure.service.DroneServiceFacade;
import com.aeroflux.drone.infrastructure.service.authorization.AuthorizationService;
import com.aeroflux.drone.infrastructure.service.geozone.GeoZoneService;
import com.aeroflux.drone.infrastructure.service.log.LogService;
import com.aeroflux.drone.infrastructure.service.navigation.NavigationService;
import com.aeroflux.drone.infrastructure.service.weather.WeatherService;

public class GeoLocationHandler implements StepHandler {

    private static final int UNAVAILABLE_DATA_TOLERANCE = 30;

    private final DroneContext context;
    private final FlightController flightController;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final LogService logService;
    private final AuthorizationService authorizationService;
    private final GeoCalculator geoCalculator;
    private final NavigationService navigationService;
    private int unavailableDataToleranceCounter = UNAVAILABLE_DATA_TOLERANCE;

    private Position lastConsideredDestinationForAuthorization;
    private Map<String, GeoZone> lastConsideredGeoZones = new HashMap<>();    

    public GeoLocationHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.flightController = droneServices.getFlightController();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.logService = droneServices.getLogService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.navigationService = droneServices.getNavigationService();
        this.geoCalculator = new GeoCalculator();
    }


    @Override
    public boolean handle() {

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        if (!flightController.isOnGround() && 
                (geoZoneService.getGeoZonesStatus() != DataStatus.AVAILABLE || 
                weatherService.getRainCellsStatus() != DataStatus.AVAILABLE ||
                authorizationService.getAuthorizationsStatus() != DataStatus.AVAILABLE)
        ) {
            unavailableDataToleranceCounter--;
            if (unavailableDataToleranceCounter <= 0) {
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.UNSAFE_FLIGHT, 
                    "Environment data not available, emergency landing requested");
                context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING_REQUEST);
            }
        } else {
            unavailableDataToleranceCounter = UNAVAILABLE_DATA_TOLERANCE;
        }

        Position destination = navigationService.getCurrentDestination();

        if(geoZoneService.getGeoZonesStatus() == DataStatus.AVAILABLE && authorizationService.getAuthorizationsStatus() == DataStatus.AVAILABLE) {

            boolean shouldRequestNewAuthorizations =
                    !(destination.equals(lastConsideredDestinationForAuthorization) &&
                    geoZoneService.getGeoZones().equals(lastConsideredGeoZones));
            if (shouldRequestNewAuthorizations) {
                authorizationService.requestLinearPathAuthorizations(
                    context.getDroneProperties().getId(),
                    flightController.getCurrentPosition(),
                    destination,
                    geoZoneService.getGeoZones());
                lastConsideredDestinationForAuthorization = destination;
                lastConsideredGeoZones = geoZoneService.getGeoZones();
            }
        }

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
        Position currentPosition = flightController.getCurrentPosition();

        boolean zoneViolation = false;
        if (geoZonesToConsider.isEmpty() && rainCellsToConsider.isEmpty()) {
            return false;
        }
        for (GeoZone geoZone : geoZonesToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, geoZone)) {
                zoneViolation = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.GEOZONE_ENTERED, 
                    "Drone entered geo zone: " + geoZone.getId() + " at position: " + currentPosition);
            }
        }

        for (RainCell rainCell : rainCellsToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, rainCell)) {
                zoneViolation = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.RAIN_CELL_ENTERED, 
                    "Drone entered rain cell at position: " + currentPosition);
            }
        }

        if (zoneViolation) {
            if (flightController.isOnGround()) {
                return true;
            }
            context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING_REQUEST);
            return false;
        }

        for (GeoZone geoZone : geoZonesToConsider) {
            if (geoCalculator.isPointInZone(destination, geoZone)) {
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.DESTINATION_IN_GEOZONE, 
                    "Destination " + destination + " is in geo zone: " + geoZone.getId() + ", requesting alternative destination");
                context.setFlightMode(DroneFlightMode.ALTERNATIVE_DESTINATION_REQUEST);
                return false;
            }
        }

        for (RainCell rainCell : rainCellsToConsider) {
            if (geoCalculator.isPointInZone(destination, rainCell)) {
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.DESTINATION_IN_RAIN_CELL, 
                    "Destination " + destination + " is in rain cell, requesting alternative destination");
                context.setFlightMode(DroneFlightMode.ALTERNATIVE_DESTINATION_REQUEST);
                return false;
            }
        }

        return false;
    }
    
}

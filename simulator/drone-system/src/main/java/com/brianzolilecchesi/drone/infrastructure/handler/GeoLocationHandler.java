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
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.domain.navigation.GeoCalculator;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class GeoLocationHandler implements StepHandler {

    private static final int UNAVAILABLE_DATA_TOLERANCE = 30;

    private final DroneContext context;
    private final FlightController flightController;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final LogService logService;
    private final AuthorizationService authorizationService;
    private final GeoCalculator geoCalculator;
    private int unavailableDataToleranceCounter = UNAVAILABLE_DATA_TOLERANCE;

    public GeoLocationHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.flightController = droneServices.getFlightController();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.logService = droneServices.getLogService();
        this.authorizationService = droneServices.getAuthorizationService();
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

        boolean needsToLand = false;
        if (geoZonesToConsider.isEmpty() && rainCellsToConsider.isEmpty()) {
            return false;
        }

        for (GeoZone geoZone : geoZonesToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, geoZone)) {
                needsToLand = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.GEOZONE_ENTERED, 
                    "Drone entered geo zone: " + geoZone.getId() + " at position: " + currentPosition);
            }
        }

        for (RainCell rainCell : rainCellsToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, rainCell)) {
                needsToLand = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.RAIN_CELL_ENTERED, 
                    "Drone entered rain cell at position: " + currentPosition);
            }
        }

        if (!needsToLand) return false;

        if (flightController.isOnGround()) {
            return true;
        }

        context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING_REQUEST);

        return false;
    }
    
}

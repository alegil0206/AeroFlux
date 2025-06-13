package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.Authorization;
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
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class GeoLocationHandler implements StepHandler {

    private final DroneContext context;
    private final FlightController flightController;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final LogService logService;
    private final NavigationService navigationService;
    private final AuthorizationService authorizationService;
    private final GeoCalculator geoCalculator;
    
    public GeoLocationHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.flightController = droneServices.getFlightController();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.logService = droneServices.getLogService();
        this.navigationService = droneServices.getNavigationService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.geoCalculator = new GeoCalculator();
    }


    @Override
    public boolean handle() {

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
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

        boolean needsToAdapt = false;
        if (geoZonesToConsider.isEmpty() && rainCellsToConsider.isEmpty()) {
            return false;
        }

        for (GeoZone geoZone : geoZonesToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, geoZone)) {
                needsToAdapt = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.GEOZONE_ENTERED, 
                    "Drone entered geo zone: " + geoZone.getId() + " at position: " + currentPosition);
            }
        }

        for (RainCell rainCell : rainCellsToConsider) {
            if (geoCalculator.isPointInZone(currentPosition, rainCell)) {
                needsToAdapt = true;
                logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.RAIN_CELL_ENTERED, 
                    "Drone entered rain cell at position: " + currentPosition);
            }
        }

        if (!needsToAdapt) return false;

        if (flightController.isOnGround()) {
            return true;
        }

        context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING);
        Position landingPosition = navigationService.configureVerticalLanding(currentPosition);
        context.setCurrentDestination(landingPosition);
        logService.info(LogConstants.Component.GEOLOCATION_HANDLER, LogConstants.Event.EMERGENCY_LANDING, 
            "Emergency landing initiated at position: " + landingPosition);

        return false;
    }
    
}

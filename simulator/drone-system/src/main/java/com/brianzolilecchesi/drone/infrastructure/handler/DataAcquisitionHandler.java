package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.HashMap;
import java.util.Map;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class DataAcquisitionHandler implements StepHandler {

    private static final int GEOZONES_UPDATE_INTERVAL = 30;
    private int lastGeoZoneUpdate = GEOZONES_UPDATE_INTERVAL;

    private static final int WEATHER_UPDATE_INTERVAL = 60;
    private int lastWeatherUpdate = WEATHER_UPDATE_INTERVAL;

    private static final int AUTHORIZATION_UPDATE_INTERVAL = 30;
    private int lastAuthorizationUpdate = AUTHORIZATION_UPDATE_INTERVAL;

    private static final int SUPPORT_POINTS_UPDATE_INTERVAL = 120;
    private int lastSupportPointsUpdate = SUPPORT_POINTS_UPDATE_INTERVAL;

    private final DroneContext context;
    private final GeoZoneService geoZoneService;
    private final AuthorizationService authorizationService;
    private final WeatherService weatherService;
    private final SupportPointService supportPointService;
    private final FlightController flightController;
    private final NavigationService navigationService;

    private Position lastConsideredDestination;
    private Map<String, GeoZone> lastConsideredGeoZones = new HashMap<>();
    

    public DataAcquisitionHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.geoZoneService = droneServices.getGeoZoneService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.weatherService = droneServices.getWeatherService();
        this.flightController = droneServices.getFlightController();
        this.supportPointService = droneServices.getSupportPointService();
        this.navigationService = droneServices.getNavigationService();
    }

    @Override
    public boolean handle() {

        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        int currentStep = context.getStep();

        DataStatus weatherStatus = weatherService.getRainCellsStatus();
        boolean shouldRefreshWeather =
                weatherStatus == DataStatus.NOT_REQUESTED ||
                weatherStatus == DataStatus.FAILED ||
                currentStep - lastWeatherUpdate >= WEATHER_UPDATE_INTERVAL;
        if (shouldRefreshWeather) {
            weatherService.fetchRainCells();
            lastWeatherUpdate = currentStep;
        }

        DataStatus supportPointStatus = supportPointService.getSupportPointsStatus();

        boolean shouldRefreshSupportPoints =
                supportPointStatus == DataStatus.NOT_REQUESTED ||
                supportPointStatus == DataStatus.FAILED ||
                currentStep - lastSupportPointsUpdate >= SUPPORT_POINTS_UPDATE_INTERVAL;

        if(shouldRefreshSupportPoints) {            
            supportPointService.fetchSupportPoints();
            lastSupportPointsUpdate = currentStep;    
        }
 
        DataStatus geoZonesStatus = geoZoneService.getGeoZonesStatus();
        boolean shouldRefreshGeoZones =
                geoZonesStatus == DataStatus.NOT_REQUESTED ||
                geoZonesStatus == DataStatus.FAILED ||
                currentStep - lastGeoZoneUpdate >= GEOZONES_UPDATE_INTERVAL;
        if (shouldRefreshGeoZones) {
            geoZoneService.fetchGeoZones();
            lastGeoZoneUpdate = currentStep;
        }

        if(geoZoneService.getGeoZonesStatus() != DataStatus.AVAILABLE) {
            return false;
        }

        Position destination = navigationService.getCurrentDestination() != null ? 
                navigationService.getCurrentDestination() :
                new Position(context.getDroneProperties().getDestination(), 0);

        boolean shouldRequestNewAuthorizations =
                !(destination.equals(lastConsideredDestination) &&
                geoZoneService.getGeoZones().equals(lastConsideredGeoZones));
        if (shouldRequestNewAuthorizations) {
            authorizationService.requestLinearPathAuthorizations(
                context.getDroneProperties().getId(),
                flightController.getCurrentPosition(),
                destination,
                geoZoneService.getGeoZones());
            lastConsideredDestination = destination;
            lastConsideredGeoZones = geoZoneService.getGeoZones();
        }

        DataStatus authorizationStatus = authorizationService.getAuthorizationsStatus();
        boolean shouldRefreshAuthorizations =
                authorizationStatus == DataStatus.NOT_REQUESTED ||
                authorizationStatus == DataStatus.FAILED ||
                authorizationStatus == DataStatus.EXPIRED ||
                currentStep - lastAuthorizationUpdate >= AUTHORIZATION_UPDATE_INTERVAL;
        if (shouldRefreshAuthorizations) {
            authorizationService.fetchAuthorizations(context.getDroneProperties().getId());
            lastAuthorizationUpdate = currentStep;
        }

        return false;
    }
}           
package com.aeroflux.drone.infrastructure.handler;

import com.aeroflux.drone.domain.handler.StepHandler;
import com.aeroflux.drone.domain.model.DataStatus;
import com.aeroflux.drone.domain.model.DroneContext;
import com.aeroflux.drone.domain.model.DroneFlightMode;
import com.aeroflux.drone.infrastructure.service.DroneServiceFacade;
import com.aeroflux.drone.infrastructure.service.authorization.AuthorizationService;
import com.aeroflux.drone.infrastructure.service.geozone.GeoZoneService;
import com.aeroflux.drone.infrastructure.service.supportPoint.SupportPointService;
import com.aeroflux.drone.infrastructure.service.weather.WeatherService;

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

    public DataAcquisitionHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.geoZoneService = droneServices.getGeoZoneService();
        this.authorizationService = droneServices.getAuthorizationService();
        this.weatherService = droneServices.getWeatherService();
        this.supportPointService = droneServices.getSupportPointService();
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
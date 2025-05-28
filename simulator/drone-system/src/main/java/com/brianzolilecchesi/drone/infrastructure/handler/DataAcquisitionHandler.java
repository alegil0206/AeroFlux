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

public class DataAcquisitionHandler implements StepHandler {

    private static final int GEOZONES_UPDATE_INTERVAL = 25;
    private int lastGeoZoneUpdate = GEOZONES_UPDATE_INTERVAL;

    private static final int WEATHER_UPDATE_INTERVAL = 25;
    private int lastWeatherUpdate = WEATHER_UPDATE_INTERVAL;

    private static final int AUTHORIZATION_UPDATE_INTERVAL = 25;
    private int lastAuthorizationUpdate = AUTHORIZATION_UPDATE_INTERVAL;

    @Override
    public boolean handle(DroneContext ctx) {

        int currentStep = ctx.stepCounter;

        if (ctx.getFlightMode() == DroneFlightMode.LANDING_CONFIGURED ||
            ctx.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        DataStatus geoZonesStatus = ctx.geoZoneService.getGeoZonesStatus();
        boolean shouldRefreshGeoZones =
                geoZonesStatus == DataStatus.NOT_REQUESTED ||
                geoZonesStatus == DataStatus.FAILED ||
                currentStep - lastGeoZoneUpdate >= GEOZONES_UPDATE_INTERVAL;
        if (shouldRefreshGeoZones) {
            ctx.geoZoneService.fetchGeoZones();
            lastGeoZoneUpdate = currentStep;
        }

        DataStatus authorizationStatus = ctx.authorizationService.getAuthorizationsStatus();
        boolean shouldRefreshAuthorizations =
                authorizationStatus == DataStatus.NOT_REQUESTED ||
                authorizationStatus == DataStatus.FAILED ||
                currentStep - lastAuthorizationUpdate >= AUTHORIZATION_UPDATE_INTERVAL;
        if (shouldRefreshAuthorizations) {
            ctx.authorizationService.fetchAuthorizations(ctx.props.getId());
            lastAuthorizationUpdate = currentStep;
        }

        DataStatus weatherStatus = ctx.geoZoneService.getGeoZonesStatus();
        boolean shouldRefreshWeather =
                weatherStatus == DataStatus.NOT_REQUESTED ||
                weatherStatus == DataStatus.FAILED ||
                currentStep - lastWeatherUpdate >= WEATHER_UPDATE_INTERVAL;
        if (shouldRefreshWeather) {
            ctx.weatherService.fetchRainCells();
            lastWeatherUpdate = currentStep;
        }

        if (weatherStatus == DataStatus.AVAILABLE) {
            ctx.weatherService.setRainCellsStatus(DataStatus.LOADED_ON_GRAPH);
            ctx.weatherNavService.clear();
            ctx.weatherNavService.add(ctx.weatherService.getRainCells());
        }

        boolean shouldLoadGeoZonesAndAuthorizations =
                (geoZonesStatus == DataStatus.AVAILABLE && authorizationStatus == DataStatus.AVAILABLE) ||
                (geoZonesStatus == DataStatus.LOADED_ON_GRAPH && authorizationStatus == DataStatus.AVAILABLE) ||
                (geoZonesStatus == DataStatus.AVAILABLE && authorizationStatus == DataStatus.LOADED_ON_GRAPH);
        if (shouldLoadGeoZonesAndAuthorizations) {
            ctx.geoZoneService.setGeoZonesStatus(DataStatus.LOADED_ON_GRAPH);
            ctx.authorizationService.setAuthorizationsStatus(DataStatus.LOADED_ON_GRAPH);

            Map<String, GeoZone> geoZones = ctx.geoZoneService.getGeoZones();
            Map<String, Authorization> authorizations = ctx.authorizationService.getAuthorizations();

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

            ctx.geozoneNavService.clear();
            ctx.geozoneNavService.add(geoZonesToConsider);        
        }
        
        return false;
    }
}

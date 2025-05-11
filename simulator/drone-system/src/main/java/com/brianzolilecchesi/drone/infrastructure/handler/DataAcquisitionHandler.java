package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;

public class DataAcquisitionHandler implements StepHandler {

    private static final int GEOZONES_UPDATE_INTERVAL = 25;
    private int lastGeoZoneUpdate = GEOZONES_UPDATE_INTERVAL;

    private static final int WEATHER_UPDATE_INTERVAL = 25;
    private int lastWeatherUpdate = WEATHER_UPDATE_INTERVAL;


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
                (geoZonesStatus == DataStatus.AVAILABLE && currentStep - lastGeoZoneUpdate >= GEOZONES_UPDATE_INTERVAL);

        if (shouldRefreshGeoZones) {
            ctx.geoZoneService.fetchGeoZones();
            lastGeoZoneUpdate = currentStep;
        }

        DataStatus weatherStatus = ctx.geoZoneService.getGeoZonesStatus();

        boolean shouldRefreshWeather =
                weatherStatus == DataStatus.NOT_REQUESTED ||
                weatherStatus == DataStatus.FAILED ||
                (weatherStatus == DataStatus.AVAILABLE && currentStep - lastWeatherUpdate >= WEATHER_UPDATE_INTERVAL);

        if (shouldRefreshWeather) {
            ctx.weatherService.fetchRainCells();
            lastWeatherUpdate = currentStep;
        }
        

        return false;
    }
}

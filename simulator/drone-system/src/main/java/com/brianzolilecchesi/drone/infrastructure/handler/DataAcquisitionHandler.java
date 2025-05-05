package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;

public class DataAcquisitionHandler implements StepHandler {

    private static final int REFRESH_EVERY_N_STEPS = 25;
    private int stepCounter = REFRESH_EVERY_N_STEPS;

    @Override
    public boolean handle(DroneContext ctx) {

        if (ctx.getFlightMode() == DroneFlightMode.LANDING_CONFIGURED ||
            ctx.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        stepCounter++;

        DataStatus geoZonesStatus = ctx.geoZoneService.getGeoZonesStatus();

        boolean shouldRefresh =
                geoZonesStatus == DataStatus.NOT_REQUESTED ||
                geoZonesStatus == DataStatus.FAILED ||
                (geoZonesStatus == DataStatus.AVAILABLE && stepCounter >= REFRESH_EVERY_N_STEPS);

        if (shouldRefresh) {
            ctx.geoZoneService.fetchGeoZones();
            stepCounter = 0;
        }

        return false;
    }
}

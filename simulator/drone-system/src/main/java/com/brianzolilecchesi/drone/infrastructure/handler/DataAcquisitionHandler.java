package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;

public class DataAcquisitionHandler implements StepHandler {

    private int stepCounter = 0;
    private static final int REFRESH_EVERY_N_STEPS = 100;
    
    @Override
    public boolean handle(DroneContext ctx) {

        stepCounter++;

        DataStatus geoZonesStatus = ctx.geoZoneService.getGeoZonesStatus();

        boolean shouldRefresh =
                geoZonesStatus == DataStatus.NOT_REQUESTED ||
                geoZonesStatus == DataStatus.FAILED ||
                (geoZonesStatus == DataStatus.AVAILABLE && stepCounter >= REFRESH_EVERY_N_STEPS);

        if (shouldRefresh) {
            ctx.logService.info("GEOZONE_HANDLER", "GeoZoneRefresh", "Refreshing geozone data...");
            ctx.geoZoneService.fetchGeoZones();
            stepCounter = 0;
        }

        return false;
    }
}

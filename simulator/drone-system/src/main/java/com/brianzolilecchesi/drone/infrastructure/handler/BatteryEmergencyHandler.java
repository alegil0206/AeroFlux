package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.Position;

public class BatteryEmergencyHandler implements StepHandler {
    
    @Override
    public boolean handle(DroneContext ctx) {
        double level = ctx.batteryService.getBatteryLevel();
        ctx.logService.info("BatteryService", "Level", "Battery: " + level);
        if (!ctx.batteryService.isBatteryCritical()) return false;

        Position pos = ctx.flightController.getCurrentPosition();
        boolean safe = ctx.landingService.evaluateLandingZone(
            new Coordinate(pos.getLatitude(), pos.getLongitude())
        );
        if (!safe) {
            ctx.logService.info("DroneSystem", "EmergencyLanding", "Unsafe zone, emergency at " + pos);
            ctx.setEmergency(true);
        } else {
            ctx.logService.info("DroneSystem", "Landing", "Safe landing at " + pos);
        }
        ctx.geozoneNavService.clearGeoZones();
        ctx.navigationService.configureVerticalLanding(pos);
        return false;
    }
}
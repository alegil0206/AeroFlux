package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;

public class BatteryEmergencyHandler implements StepHandler {
    
    @Override
    public boolean handle(DroneContext ctx) {
        double level = ctx.batteryService.getBatteryLevel();
        ctx.logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CHECK, "Battery level: " + level);

        if (!ctx.batteryService.isBatteryCritical() ||
            ctx.getFlightMode() == DroneFlightMode.LANDING_CONFIGURED ||
            ctx.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        ctx.logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CRITICAL, "Battery critical, emergency landing initiated");

        Position pos = ctx.flightController.getCurrentPosition();
        boolean safe = ctx.landingService.evaluateLandingZone(
            new Coordinate(pos.getLatitude(), pos.getLongitude())
        );
        if (!safe) {
            ctx.logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.EMERGENCY_LANDING, "Unsafe zone, emergency at " + pos);
            ctx.setFlightMode(DroneFlightMode.EMERGENCY_LANDING);
        } else {
            ctx.logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.LANDING, "Safe landing at " + pos);
            ctx.setFlightMode(DroneFlightMode.LANDING_CONFIGURED);
        }
        ctx.geozoneNavService.clear();
        ctx.weatherNavService.clear();
        ctx.navigationService.configureVerticalLanding(pos);
        return false;
    }
}
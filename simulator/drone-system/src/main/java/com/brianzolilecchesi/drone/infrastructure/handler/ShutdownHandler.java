package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.LogConstants;

public class ShutdownHandler implements StepHandler {

    @Override
    public boolean handle(DroneContext ctx) {
        if (!ctx.flightController.isPoweredOn()) {
            return true;
        }
        if (ctx.navigationService.hasReached(ctx.props.getDestination()) &&
            ctx.flightController.isOnGround()) {
            ctx.logService.info("DroneSystem", "Shutdown", "Destination reached, powering off");
            ctx.flightController.powerOff();
            return true;
        }
        if (ctx.batteryService.isBatteryCritical() && ctx.flightController.isOnGround()) {
            ctx.logService.info(LogConstants.Service.DRONE_SYSTEM, "Landed", "Drone is on the ground and battery is critical.");
            ctx.flightController.powerOff();
            return true;
        }
        return false;
    }
}

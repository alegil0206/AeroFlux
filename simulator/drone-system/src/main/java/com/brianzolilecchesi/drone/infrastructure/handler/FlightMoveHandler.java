package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;

public class FlightMoveHandler implements StepHandler {
    @Override
    public boolean handle(DroneContext ctx) {
        if (ctx.navigationService.getFlightPlanStatus() == DataStatus.AVAILABLE) {
            ctx.flightController.moveTo(ctx.navigationService.followFlightPlan());
        } else {
            ctx.logService.info("DroneSystem", "FlightPlan", "Not available, hovering");
            ctx.flightController.hover();
        }
        return false;
    }
}

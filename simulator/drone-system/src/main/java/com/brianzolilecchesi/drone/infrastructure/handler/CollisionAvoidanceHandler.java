package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public class CollisionAvoidanceHandler implements StepHandler {
    
    @Override
    public boolean handle(DroneContext ctx) {
        Position next = ctx.navigationService.getNextPosition();
        for (NearbyDroneStatus other : ctx.communicationService.getNearbyDroneStatus()) {
            if (other.getNextPosition().distance(next) < 20.0) {
                boolean yield = ctx.precedenceService.shouldYield(
                    ctx.props.getId(), other.getDroneId()
                );
                if (yield) {
                    ctx.logService.info("DroneSystem", "CollisionAvoidance", "Yielding to " + other.getDroneId());
                    ctx.flightController.hover();
                    return true;
                }
            }
        }
        return false;
    }
}
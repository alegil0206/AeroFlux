package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.Position;

public class FlightPlanningHandler implements StepHandler {

    private static final int OPTIMIZE_EVERY_N_STEPS = 50;
    private int stepCounter = 0;

    @Override
    public boolean handle(DroneContext ctx) {

        if (ctx.getFlightMode() == DroneFlightMode.LANDING_CONFIGURED ||
            ctx.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        DataStatus status = ctx.navigationService.getFlightPlanStatus();

        if (status == DataStatus.LOADING) {
            if (ctx.flightController.isOnGround())
                return true; 
            else 
                return false;
        }

        boolean dataAvailable = ctx.geoZoneService.getGeoZonesStatus() == DataStatus.LOADED_ON_GRAPH &&
            ctx.weatherService.getRainCellsStatus() == DataStatus.LOADED_ON_GRAPH &&
            ctx.authorizationService.getAuthorizationsStatus() == DataStatus.LOADED_ON_GRAPH;

        if (status == DataStatus.AVAILABLE) {
            stepCounter++;
            if (stepCounter >= OPTIMIZE_EVERY_N_STEPS) {
                if (dataAvailable) {
                    ctx.navigationService.optimizeFlightPlan();
                    stepCounter = 0;
                }
            }
            return false;
        }

        if (dataAvailable) {
            ctx.navigationService.generateFlightPlan(
                new Position(ctx.props.getSource(), 0),
                new Position(ctx.props.getDestination(), 0)
            );
            stepCounter = 0;
        }
        return true;
    }
    
}



package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;

public class FlightControlHandler implements StepHandler {

    private final DroneContext context;
    private final FlightController flightController;
    private final NavigationService navigationService;
    private final LogService logService;

    public FlightControlHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.flightController = droneServices.getFlightController();
        this.navigationService = droneServices.getNavigationService();
        this.logService = droneServices.getLogService();
    }

    @Override
    public boolean handle() {

        if (!flightController.isPoweredOn()) {
            return true;
        }

        if (navigationService.getFlightPlanStatus() == DataStatus.AVAILABLE) {
            Position nextPosition = navigationService.followFlightPlan();
            if (nextPosition != null)
                flightController.moveTo(nextPosition);
        } else {
            flightController.hover();
        }

        if (navigationService.hasReached(flightController.getCurrentPosition(), context.getDroneProperties().getDestination()) &&
            flightController.isOnGround()) {
            
            logService.info(LogConstants.Component.FLIGHT_CONTROL_HANDLER, LogConstants.Event.DESTINATION_REACHED, "Destination reached, powering off");
            flightController.powerOff();
            context.setFlightMode(DroneFlightMode.FLIGHT_COMPLETED);
            return true;
        }

        if (navigationService.hasReached(flightController.getCurrentPosition(), context.getCurrentDestination()) && 
            flightController.isOnGround()) {

            logService.info(LogConstants.Component.FLIGHT_CONTROL_HANDLER, LogConstants.Event.DESTINATION_REACHED, "Reached current destination: " + context.getCurrentDestination() + ", powering off");
            flightController.powerOff();
            context.setFlightMode(DroneFlightMode.REROUTE_FLIGHT);
            return true;
        }

        return false;
    }
}

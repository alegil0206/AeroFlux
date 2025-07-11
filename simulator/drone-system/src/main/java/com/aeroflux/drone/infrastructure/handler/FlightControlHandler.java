package com.aeroflux.drone.infrastructure.handler;

import com.aeroflux.drone.domain.handler.StepHandler;
import com.aeroflux.drone.domain.model.DataStatus;
import com.aeroflux.drone.domain.model.DroneContext;
import com.aeroflux.drone.domain.model.DroneFlightMode;
import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.infrastructure.controller.FlightController;
import com.aeroflux.drone.infrastructure.service.DroneServiceFacade;
import com.aeroflux.drone.infrastructure.service.log.LogService;
import com.aeroflux.drone.infrastructure.service.navigation.NavigationService;

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
            if (flightController.isOnGround()) {
                logService.info(LogConstants.Component.FLIGHT_CONTROL_HANDLER, LogConstants.Event.WAITING, "Waiting on ground");
            } else {
                flightController.hover();
            }
        }

        if (navigationService.hasReached(flightController.getCurrentPosition(), new Position(context.getDroneProperties().getDestination(), 0)) &&
            flightController.isOnGround()) {
            
            logService.info(LogConstants.Component.FLIGHT_CONTROL_HANDLER, LogConstants.Event.DESTINATION_REACHED, "Destination reached, powering off");
            flightController.powerOff();
            context.setFlightMode(DroneFlightMode.FLIGHT_COMPLETED);
            return true;
        }

        if (navigationService.hasReached(flightController.getCurrentPosition(), navigationService.getCurrentDestination()) && 
            flightController.isOnGround()) {

            logService.info(LogConstants.Component.FLIGHT_CONTROL_HANDLER, LogConstants.Event.DESTINATION_REACHED, "Reached current destination: " + navigationService.getCurrentDestination() + ", powering off");
            flightController.powerOff();
             
            return true;
        }

        return false;
    }
}

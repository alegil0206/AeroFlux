package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.DroneSafetyNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;

public class ConflictAvoidanceHandler implements StepHandler {

    private final DroneContext context;
    private final CommunicationService communicationService;
    private final LogService logService;
    private final DroneSafetyNavigationService droneSafetyNavigationService;
    private final FlightController flightController;
    private final NavigationService navigationService;

    public ConflictAvoidanceHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.communicationService = droneServices.getCommunicationService();
        this.logService = droneServices.getLogService();
        this.droneSafetyNavigationService = droneServices.getDroneSafetyNavigationService();
        this.flightController = droneServices.getFlightController();
        this.navigationService = droneServices.getNavigationService();
    }

    @Override
    public boolean handle() {

        List<NearbyDroneStatus> nearbyDrones = communicationService.getNearbyDroneStatus();
        List<NearbyDroneStatus> dronesToAvoid = new ArrayList<>();
        boolean needToHover = false;

        if (nearbyDrones.isEmpty()) {
            return false;
        }

        NearbyDroneStatus droneStatus = new NearbyDroneStatus(
                context.getDroneProperties().getId(),
                context.getDroneProperties().getOperationCategory(),
                context.getFlightMode(),
                flightController.getCurrentPosition(),
                navigationService.getNextWaypoints());

        for (NearbyDroneStatus other : nearbyDrones) {
            boolean hasPriority = droneSafetyNavigationService.hasPriority(droneStatus, other);

            if (droneSafetyNavigationService.isInConflictVolume(droneStatus.getPosition(), other.getPosition())) {
                logService.info(LogConstants.Component.COLLISION_AVOIDANCE_HANDLER, LogConstants.Event.COLLISION_AVOIDANCE,
                        "Drone is in collision with: " + other.getDroneId() + " - Distance: "
                                + droneStatus.getPosition().distance(other.getPosition()));
            }

            if (droneSafetyNavigationService.isInSelfSeparationVolume(droneStatus.getPosition(), other.getPosition())) {

                if (droneSafetyNavigationService.conflictCondition(droneStatus.getNextPositions(), other.getNextPositions())) {

                    if (droneSafetyNavigationService.headToHeadCondition(droneStatus.getNextPositions(), other.getNextPositions())) {

                        if (hasPriority) {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                            needToHover = true;
                        } else {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Drone hasn't priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                            dronesToAvoid.add(other);
                        }
                    } else {
                        if (hasPriority) {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                        } else {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Drone hasn't priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition()
                                                    .distance(droneStatus.getPosition()));
                            needToHover = true;
                        }
                    }
                }
            }
        }

        if (dronesToAvoid.isEmpty()) {
            droneSafetyNavigationService.setConflictingDrones(new ArrayList<>());
            if (needToHover) {
                flightController.hover();
                return true;
            }
            return false;
        }

        droneSafetyNavigationService.setConflictingDrones(dronesToAvoid);
        return false;

    }

}
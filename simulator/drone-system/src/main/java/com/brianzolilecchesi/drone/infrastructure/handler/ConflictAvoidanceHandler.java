package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.List;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;

public class ConflictAvoidanceHandler implements StepHandler {


    @Override
    public boolean handle(DroneContext ctx) {

        List<NearbyDroneStatus> nearbyDrones = ctx.communicationService.getNearbyDroneStatus();

        if (nearbyDrones.isEmpty()) {
            return false;
        }

        NearbyDroneStatus droneStatus = new NearbyDroneStatus(
                ctx.props.getId(),
                ctx.props.getOperationCategory(),
                ctx.getFlightMode(),
                ctx.flightController.getCurrentPosition(),
                ctx.navigationService.getNextPositions());

        for (NearbyDroneStatus other : nearbyDrones) {
            boolean hasPriority = ctx.precedenceService.hasPriority(droneStatus, other);

            if (ctx.precedenceService.isInConflictVolume(droneStatus.getPosition(), other.getPosition())) {
                ctx.logService.info(LogConstants.Component.COLLISION_AVOIDANCE_HANDLER, LogConstants.Event.COLLISION_AVOIDANCE,
                        "Drone is in collision with: " + other.getDroneId() + " - Distance: "
                                + droneStatus.getPosition().distance(other.getPosition()));
            }

            if (ctx.precedenceService.isInSelfSeparationVolume(droneStatus.getPosition(), other.getPosition())) {

                if (ctx.precedenceService.conflictCondition(droneStatus.getNextPositions(), other.getNextPositions())) {

                    if (ctx.precedenceService.headToHeadCondition(droneStatus.getNextPositions(), other.getNextPositions())) {

                        if (hasPriority) {
                            ctx.logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Hovering - Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                        } else {
                            ctx.logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Hovering and replanning - Drone hasn't priority over: " + other.getDroneId());
                            if (ctx.navigationService.getFlightPlanStatus() != DataStatus.LOADING) {
                                if (ctx.geoZoneService.getGeoZonesStatus() == DataStatus.AVAILABLE) {
                                    ctx.geozoneNavService.clearGeoZones();
                                    List<GeoZone> zones = ctx.geoZoneService.getGeoZones();
                                    zones.removeIf(zone -> zone.getStatus().equals(GeoZone.GEOZONE_INACTIVE_STATUS));
                                    ctx.geozoneNavService.addGeoZones(zones);
                                    ctx.navigationService.adaptFlightPlan();
                                }
                            }
                        }
                        ctx.flightController.hover();
                        return true;
                    } else {
                        if (hasPriority) {
                            ctx.logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Free to fly - Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                        } else {
                            ctx.logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Hovering - Drone hasn't priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition()
                                                    .distance(droneStatus.getPosition()));
                            ctx.flightController.hover();
                            return true;
                        }
                    }
                }

            }
        }

        return false;

    }


}
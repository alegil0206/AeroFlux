package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.Authorization;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.DroneSafetyNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class ConflictAvoidanceHandler implements StepHandler {

    private final DroneContext context;
    private final CommunicationService communicationService;
    private final LogService logService;
    private final DroneSafetyNavigationService droneSafetyNavigationService;
    private final FlightController flightController;
    private final NavigationService navigationService;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final AuthorizationService authorizationService;

    public ConflictAvoidanceHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.communicationService = droneServices.getCommunicationService();
        this.logService = droneServices.getLogService();
        this.droneSafetyNavigationService = droneServices.getDroneSafetyNavigationService();
        this.flightController = droneServices.getFlightController();
        this.navigationService = droneServices.getNavigationService();
        this.geoZoneService = droneServices.getGeoZoneService();
        this.weatherService = droneServices.getWeatherService();
        this.authorizationService = droneServices.getAuthorizationService();
    }

    @Override
    public boolean handle() {

        List<NearbyDroneStatus> nearbyDrones = communicationService.getNearbyDroneStatus();

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
                                    "Hovering - Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                        } else {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Hovering and replanning - Drone hasn't priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                            if (navigationService.getFlightPlanStatus() != DataStatus.LOADING &&
                                geoZoneService.getGeoZonesStatus() == DataStatus.AVAILABLE &&
                                weatherService.getRainCellsStatus() == DataStatus.AVAILABLE &&
                                authorizationService.getAuthorizationsStatus() == DataStatus.AVAILABLE) {
                                    Map<String, GeoZone> geoZones = geoZoneService.getGeoZones();
                                    Map<String, Authorization> authorizations = authorizationService.getAuthorizations();

                                    List<GeoZone> geoZonesToConsider = new ArrayList<>();
                                    for (Entry<String, GeoZone> entry : geoZones.entrySet()) {
                                        String geoZoneId = entry.getKey();
                                        GeoZone geoZone = entry.getValue();
                                        if (geoZone.isActive()) {
                                            Authorization auth = authorizations.get(geoZoneId);
                                            if (auth == null || !auth.isGranted()) {
                                                geoZonesToConsider.add(geoZone);
                                            }
                                        }
                                    }                                    
                                    navigationService.adaptFlightPlan(
                                        context.getCurrentDestination(), 
                                        geoZonesToConsider,
                                        weatherService.getRainCells(),
                                        other);
                            }
                        }
                        flightController.hover();
                        return true;
                    } else {
                        if (hasPriority) {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Free to fly - Drone has priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition().distance(droneStatus.getPosition()));
                        } else {
                            logService.info(LogConstants.Component.CONFLICT_AVOIDANCE_HANDLER, LogConstants.Event.CONFLICT_AVOIDANCE,
                                    "Hovering - Drone hasn't priority over: " + other.getDroneId() + " - Distance: "
                                            + other.getPosition()
                                                    .distance(droneStatus.getPosition()));
                            flightController.hover();
                            return true;
                        }
                    }
                }

            }
        }

        return false;

    }


}
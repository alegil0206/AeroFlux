package com.brianzolilecchesi.drone;

import java.util.Collections;
import java.util.List;
import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.Battery;
import com.brianzolilecchesi.drone.domain.component.Camera;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.component.Motor;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryMonitor;
import com.brianzolilecchesi.drone.infrastructure.service.communication.RadioService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.landing.SafeLandingService;
import com.brianzolilecchesi.drone.infrastructure.service.log.StepLogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.FlightNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.GeozoneNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAuthorizationRestClient;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAwarenessRestClient;
import com.brianzolilecchesi.drone.infrastructure.integration.WeatherServiceRestClient;


public class DroneSystem {

    private final DroneProperties droneProperties;
    private final HardwareAbstractionLayer hardwareAbstractionLayer;
    private final BatteryService batteryService;
    private final CommunicationService communicationService;
    private final LandingService landingService;
    private final NavigationService navigationService;
    private final FlightController flightController;
    private final LogService logService;
    private final GeoZoneService geoZoneService;
    private final GeozoneNavigationService geozoneNavigationService;
    private final WeatherService weatherService;
    private final AuthorizationService authorizationService;
    private final SupportPointService supportPointService;
    
	private int i = 0;
    private boolean isEmergency = false;
    
    public DroneSystem(
            DroneProperties droneProperties,
            HardwareAbstractionLayer hardwareAbstractionLayer
            ) {
        this.droneProperties = droneProperties;
        this.hardwareAbstractionLayer = hardwareAbstractionLayer;
        this.logService = new StepLogService(droneProperties.getId());
        this.batteryService = new BatteryMonitor(hardwareAbstractionLayer.getBattery(), logService);
        this.communicationService = new RadioService(hardwareAbstractionLayer.getRadio(), logService);
        this.landingService = new SafeLandingService(hardwareAbstractionLayer.getCamera(), logService);
        this.navigationService = new FlightNavigationService(hardwareAbstractionLayer.getGps(), hardwareAbstractionLayer.getAltimeter(), logService);
        this.geozoneNavigationService = new GeozoneNavigationService();
        this.flightController = new FlightController(hardwareAbstractionLayer.getMotor(), hardwareAbstractionLayer.getGps(), hardwareAbstractionLayer.getAltimeter(), logService);

        this.geoZoneService = new GeoZoneService();
        this.weatherService = new WeatherService();
        this.authorizationService= new AuthorizationService();
        this.supportPointService = new SupportPointService();
    }

    public DroneStatus executeStep() {

        if (flightController.isPoweredOn()) {

            logService.info(LogConstants.Service.DRONE_SYSTEM, "Step", "Executing step " + i++);

            if ( navigationService.hasReached(droneProperties.getDestination()) && navigationService.isOnGround()) {
                flightController.powerOff();
            } else if(batteryService.isBatteryCritical() && navigationService.isOnGround()) {
                logService.info(LogConstants.Service.DRONE_SYSTEM, "Landed", "Drone is on the ground and battery is critical.");
                flightController.powerOff();
            } else if (navigationService.isOnGround() && navigationService.getFlightPlanStatus() != DataStatus.AVAILABLE) {

                if(navigationService.getFlightPlanStatus() == DataStatus.NOT_REQUESTED || navigationService.getFlightPlanStatus() == DataStatus.FAILED || navigationService.getFlightPlanStatus() == DataStatus.EXPIRED) {
                    DataStatus geoZoneDataStatus = geoZoneService.getGeoZonesStatus();
                    if (geoZoneDataStatus == DataStatus.NOT_REQUESTED || geoZoneDataStatus == DataStatus.EXPIRED) {
                        geoZoneService.fetchGeoZones();
                    } else if (geoZoneDataStatus == DataStatus.FAILED) {
                        geoZoneService.fetchGeoZones();
                    } else if (geoZoneDataStatus == DataStatus.AVAILABLE) {
                        List<GeoZone> geoZones = geoZoneService.getGeoZones();
                        geozoneNavigationService.addGeoZones(geoZones);
                        navigationService.calculateFlightPlan( 
                                new Position(droneProperties.getSource(), 0),
                                new Position(droneProperties.getDestination(), 0)
                                );
                    }          
                }
            } else {

                double batteryLevel = batteryService.getBatteryLevel();
                logService.info(LogConstants.Service.BATTERY_SERVICE, "Battery", "Battery level: " + batteryLevel);

                if (batteryService.isBatteryCritical()) {
                    logService.info(LogConstants.Service.BATTERY_SERVICE, "Battery", "Battery level is critical: " + batteryLevel);
                    Position currentPosition = navigationService.getCurrentPosition();
                    Boolean isSafe = landingService.evaluateLandingZone(new Coordinate(currentPosition.getLatitude(), currentPosition.getLongitude()));
                    if (isSafe) {
                        logService.info(LogConstants.Service.DRONE_SYSTEM, "Landing", "Configuring landing at position: " + currentPosition);
                    } else {
                        logService.info(LogConstants.Service.DRONE_SYSTEM, "Emergency Landing", "Configuring emergency landing at position: " + currentPosition);
                        isEmergency = true;
                    }
                    navigationService.configureVerticalLanding();
                }

                Position nextPosition = navigationService.getNextPosition();

                List<NearbyDroneStatus> nearbyDrones = communicationService.getNearbyDroneStatus();
                boolean droneProximityWarning = false;
                for (NearbyDroneStatus nearbyDrone : nearbyDrones) {
                    if(nearbyDrone.getNextPosition().distance(nextPosition) < 20.0) {
                        // check precedenze                        
                        droneProximityWarning = true;
                        break;
                    }
                }

                if (droneProximityWarning) {
                    logService.info(LogConstants.Service.DRONE_SYSTEM, "Drone Proximity", "Drone is too close to another drone.");
                    flightController.hover();

                } else {
                    if (navigationService.getFlightPlanStatus() == DataStatus.AVAILABLE) {
                        flightController.moveTo(navigationService.followFlightPlan());               
                    } else {
                        logService.info(LogConstants.Service.DRONE_SYSTEM, "Flight Plan", "Flight plan is not available.");
                        flightController.hover();
                    }
                }
            }

            communicationService.sendDroneStatus(new NearbyDroneStatus(droneProperties.getId(), false, navigationService.getCurrentPosition(), navigationService.getNextPosition()));
            return getDroneStatus();
        }
        return null;
    }

    public DroneStatus getDroneStatus() {
        return new DroneStatus(
                droneProperties.getId(),
                navigationService.getCurrentPosition(),
                batteryService.getBatteryLevel(),
                navigationService.getFlightPlan() != null ? navigationService.getFlightPlan().getPositions() : Collections.emptyList(),
                logService.extractLogEntries()
        );
    }

    public DroneProperties getDroneProperties() {
        return droneProperties;
    }

    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return hardwareAbstractionLayer;
    }
    
    public void powerOn() {
        flightController.powerOn();
    }

    public void powerOff() {
        flightController.powerOff();
    }

}


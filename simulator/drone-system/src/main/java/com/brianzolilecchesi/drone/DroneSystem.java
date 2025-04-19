package com.brianzolilecchesi.drone;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.Battery;
import com.brianzolilecchesi.drone.domain.component.Camera;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.component.Motor;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
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
    private final WeatherServiceRestClient weatherServiceRestClient;
    private final GeoAwarenessRestClient geoAwarenessServiceRestClient;
    private final GeoAuthorizationRestClient geoAuthorizationRestClient;
    
    private String log;
	private int i = 0;
    
    public DroneSystem(
            DroneProperties droneProperties,
            Battery battery,
            Radio radio,
            Camera camera,
            GPS gps,
            Altimeter altimeter, 
            Motor motor ) {
        this.droneProperties = droneProperties;
        this.hardwareAbstractionLayer = new HardwareAbstractionLayer(battery, radio, camera, gps, altimeter, motor);
        this.logService = new StepLogService(droneProperties.getId());
        this.batteryService = new BatteryMonitor(battery, logService);
        this.communicationService = new RadioService(radio, logService);
        this.landingService = new SafeLandingService(camera, logService);
        this.navigationService = new FlightNavigationService(gps, altimeter, logService);
        this.geozoneNavigationService = new GeozoneNavigationService();
        this.flightController = new FlightController(motor, gps, altimeter, logService);
        this.weatherServiceRestClient = new WeatherServiceRestClient();
        this.geoAwarenessServiceRestClient = new GeoAwarenessRestClient();
        this.geoAuthorizationRestClient= new GeoAuthorizationRestClient() ;
        this.geoZoneService = new GeoZoneService(geoAwarenessServiceRestClient);
        this.weatherService = new WeatherService(weatherServiceRestClient);
        this.authorizationService= new AuthorizationService(geoAuthorizationRestClient);
        this.supportPointService = new SupportPointService(geoAwarenessServiceRestClient);
    }

    public DroneStatus executeStep() {

        log = "Drone " + droneProperties.getId() + ". Executing step " + i++ + ". ";

        if (i == 1){
            CompletableFuture.runAsync(() -> {
                System.out.println("richiesta a ciclo " + i);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("concluso a ciclo " + i);
            });
        }

        
        if ( navigationService.hasReached(new Position(droneProperties.getDestination(), 0 ))) {
            if (flightController.isPoweredOn()) {
                log += "Drone has reached the destination.";
                flightController.powerOff();
            } else {
                return null;
            }
        } else if(batteryService.isBatteryCritical() && navigationService.getCurrentPosition().getAltitude() < 0.1){
            if (flightController.isPoweredOn()) {
                log += "Battery is critical and drone is on the ground.";
                flightController.powerOff();
            } else {
                return null;
            }
        } else {
            if (!flightController.isPoweredOn()) {
                flightController.powerOn();
                log += "Drone is powered on. ";
                
                geoZoneService.fetchGeoZones();
                List<GeoZone> geoZones = geoZoneService.getGeoZones();
                geozoneNavigationService.addGeoZones(geoZones);
                navigationService.calculateFlightPlan( 
                		new Position(droneProperties.getSource(), 0),
                        new Position(droneProperties.getDestination(), 0)
                        );
            }

            double batteryLevel = batteryService.getBatteryLevel();
            log += " Battery level: " + batteryLevel + ". ";

            if (batteryService.isBatteryCritical()) {
                log += "Battery is critical. ";
                Position currentPosition = navigationService.getCurrentPosition();
                Position placeToLand = new Position(currentPosition.getLatitude(), currentPosition.getLongitude(), 0);
                navigationService.calculateFlightPlan(currentPosition, placeToLand);
            }

            communicationService.sendMessage("Drone position: " + navigationService.getCurrentPosition());
            log += "Received message: " + communicationService.getMessages() + ". ";

            Position nextPosition = navigationService.followFlightPlan();

            flightController.moveTo(nextPosition);
            log += "Drone moved to position: " + nextPosition + ". ";
        }

        return getDroneStatus();
    }

    public DroneStatus getDroneStatus() {
        return new DroneStatus(
                droneProperties.getId(),
                navigationService.getCurrentPosition(),
                batteryService.getBatteryLevel(),
                navigationService.getFlightPlan().getPositions(),
                log
        );
    }

    public DroneProperties getDroneProperties() {
        return droneProperties;
    }

    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return hardwareAbstractionLayer;
    }
    
} 

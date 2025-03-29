package com.brianzolilecchesi.drone;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.Battery;
import com.brianzolilecchesi.drone.domain.component.Camera;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.component.Motor;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryMonitor;
import com.brianzolilecchesi.drone.infrastructure.service.communication.RadioService;
import com.brianzolilecchesi.drone.infrastructure.service.landing.SafeLandingService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.FlightNavigationService;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;


public class DroneSystem {

    private final DroneProperties droneProperties;
    private final HardwareAbstractionLayer hardwareAbstractionLayer;
    private final BatteryService batteryService;
    private final CommunicationService communicationService;
    private final LandingService landingService;
    private final NavigationService navigationService;
    private final FlightController flightController;
    
    private String message;
    private PropertyChangeSupport support;
	private int i = 0;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    
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
        this.batteryService = new BatteryMonitor(battery);
        this.communicationService = new RadioService(radio);
        this.landingService = new SafeLandingService(camera);
        this.navigationService = new FlightNavigationService(gps, altimeter);
        this.flightController = new FlightController(motor, gps, altimeter);
        this.support = new PropertyChangeSupport(this);

        navigationService.calculateFlightPlan( new Position(droneProperties.getSource(), 0 ),
                new Position(droneProperties.getDestination(), 0 ));
    }

    public void executeStep() {

        message = "Drone " + droneProperties.getId() + ". Executing step " + i++ + ". ";

        if ( navigationService.hasReached(new Position(droneProperties.getDestination(), 0 ))) {
            if (flightController.isPoweredOn()) {
                flightController.powerOff();
            }
            message += "Drone has reached the destination.";
        } else {

            if (!flightController.isPoweredOn()) {
                flightController.powerOn();
                message += "Drone is powered on. ";
            }

            double batteryLevel = batteryService.getBatteryLevel();
            message = " Battery level: " + batteryLevel + ". ";

            communicationService.sendMessage("Drone position: " + navigationService.getCurrentPosition());
            message += "Received message: " + communicationService.getMessages() + ". ";

            Position nextPosition = navigationService.followFlightPlan();

            flightController.moveTo(nextPosition);
            message += "Drone moved to position: " + nextPosition + ". ";
        }
        support.firePropertyChange("message", null, message);
    }

    public DroneProperties getDroneProperties() {
        return droneProperties;
    }

    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return hardwareAbstractionLayer;
    }
    
} 

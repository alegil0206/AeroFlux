package com.aeroflux.drone.infrastructure.controller;

import com.aeroflux.drone.domain.component.Motor;
import com.aeroflux.drone.domain.component.Altimeter;
import com.aeroflux.drone.domain.component.GPS;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.infrastructure.service.log.LogService;
import com.aeroflux.drone.domain.model.LogConstants;

public class FlightController {
    private final LogService logService;
    private final Motor motor;
    private final GPS gps;
    private final Altimeter altimeter;
    
    public FlightController(Motor motor, GPS gps, Altimeter altimeter, LogService logService) {
        this.gps = gps;
        this.altimeter = altimeter;
        this.motor = motor;
        this.logService = logService;
    }

    public void powerOn() {
        motor.start();
        logService.info(LogConstants.Component.FLIGHT_CONTROLLER, LogConstants.Event.POWER_ON, "Powering on the drone");
    }

    public void powerOff() {
        motor.stop();
        logService.info(LogConstants.Component.FLIGHT_CONTROLLER, LogConstants.Event.SHUTDOWN, "Powering off the drone");
    }

    public boolean isPoweredOn() {
        return motor.isMotorOn();
    }

    public void moveTo(Position position ){
        motor.move(position);
        logService.info(LogConstants.Component.FLIGHT_CONTROLLER, LogConstants.Event.MOVING, "Moving to position: " + position);
    }

    public void hover() {
        motor.hover();
        logService.info(LogConstants.Component.FLIGHT_CONTROLLER, LogConstants.Event.HOVERING, "Hovering in place");
    }

    public Position getCurrentPosition() {
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        double altitude = altimeter.getAltitude();
        return new Position(latitude, longitude, altitude);
    }

    public boolean isOnGround() {
        return altimeter.getAltitude() <= 0.1;
    }

}

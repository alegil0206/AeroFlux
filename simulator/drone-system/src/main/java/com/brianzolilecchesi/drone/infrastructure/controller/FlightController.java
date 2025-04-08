package com.brianzolilecchesi.drone.infrastructure.controller;

import com.brianzolilecchesi.drone.domain.component.Motor;
import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.service.log.LogService;

public class FlightController {
    private final LogService logService;
    private final Motor motor;
    private final GPS gps;
    private final Altimeter altimeter;
    
    // private static final double EARTH_RADIUS = 6378137.0; 

    public FlightController(Motor motor, GPS gps, Altimeter altimeter, LogService logService) {
        this.gps = gps;
        this.altimeter = altimeter;
        this.motor = motor;
        this.logService = logService;
    }

    public void powerOn() {
        motor.start();
    }

    public void powerOff() {
        motor.stop();
    }

    public boolean isPoweredOn() {
        return motor.isMotorOn();
    }

    public void moveTo(Position position ){
        motor.move(position);
    }

    /*
    public void moveTo(Position position){
        double targetLatitude = position.getLatitude();
        double targetLongitude = position.getLongitude();
        double targetAltitude = position.getAltitude();
        double currentLatitude = gps.getLatitude();
        double currentLongitude = gps.getLongitude();
        double currentAltitude = altimeter.getAltitude();

        double distance = calculateDistance(currentLatitude, currentLongitude, targetLatitude, targetLongitude);
        double bearing = calculateBearing(currentLatitude, currentLongitude, targetLatitude, targetLongitude);
        double deltaAltitude = targetAltitude - currentAltitude;

        motor.move(distance, bearing, deltaAltitude);

    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double x = Math.sin(dLon) * Math.cos(lat2);
        double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        return Math.atan2(x, y);
    }
    */

}

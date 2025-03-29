package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.Motor;


public class SimulatedMotor implements Motor {
    
    private static final double EARTH_RADIUS = 6378137.0; 
    private boolean motorOn;
    private SimulatedGPS gps;
    private SimulatedAltimeter altimeter;

    public SimulatedMotor(SimulatedGPS gps, SimulatedAltimeter altimeter) {
        motorOn = false;
        this.gps = gps;
        this.altimeter = altimeter;
    }

    @Override
    public boolean isMotorOn() {
        return motorOn;
    }

    @Override
    public void start() {
        motorOn = true;
    }

    @Override
    public void stop() {
        motorOn = false;
    }

    @Override
    public void move(double distance, double bearing, double altitude) {
        if (motorOn) {
            double currentLatitude = gps.getLatitude();
            double currentLongitude = gps.getLongitude();
            double currentAltitude = altimeter.getAltitude();

            double newLatitude = Math.asin(Math.sin(Math.toRadians(currentLatitude)) * Math.cos(distance / EARTH_RADIUS)
                    + Math.cos(Math.toRadians(currentLatitude)) * Math.sin(distance / EARTH_RADIUS) * Math.cos(bearing));
            double newLongitude = Math.toRadians(currentLongitude) + Math.atan2(Math.sin(bearing) * Math.sin(distance / EARTH_RADIUS) * Math.cos(Math.toRadians(currentLatitude)),
                    Math.cos(distance / EARTH_RADIUS) - Math.sin(Math.toRadians(currentLatitude)) * Math.sin(newLatitude));
            newLongitude = Math.toDegrees(newLongitude);
            newLatitude = Math.toDegrees(newLatitude);

            double newAltitude = currentAltitude + altitude;
            gps.setLatitude(newLatitude);
            gps.setLongitude(newLongitude);
            altimeter.setAltitude(newAltitude);
        }
    }
    
}

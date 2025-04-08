package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.model.Coordinate;

public class SimulatedGPS implements GPS {
    
    private double latitude;
    private double longitude;

    public SimulatedGPS() {
    }

    public SimulatedGPS(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Coordinate getCoordinates() {
        return new Coordinate(latitude, longitude);
    }
}

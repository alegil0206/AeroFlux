package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.Altimeter;

public class SimulatedAltimeter implements Altimeter {
    
    private double altitude;

    public SimulatedAltimeter() {
    }

    public SimulatedAltimeter(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
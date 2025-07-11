package com.aeroflux.simulator.model.drone;

import com.aeroflux.drone.domain.component.Altimeter;

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
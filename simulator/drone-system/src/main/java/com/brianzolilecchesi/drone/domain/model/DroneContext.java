package com.brianzolilecchesi.drone.domain.model;

public class DroneContext {
    private final DroneProperties properties;
    private DroneFlightMode flightMode;
    private int stepCounter;

    public DroneContext(DroneProperties props) {
        this.properties = props;
        this.stepCounter = 0;
        this.flightMode = DroneFlightMode.NORMAL_FLIGHT;
    }

    public DroneProperties getDroneProperties() {
        return properties;
    }

    public int nextStep() {
        return stepCounter++;
    }

    public int getStep() {
        return stepCounter;
    }

    public DroneFlightMode getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(DroneFlightMode flightMode) {
        this.flightMode = flightMode;
    }
}
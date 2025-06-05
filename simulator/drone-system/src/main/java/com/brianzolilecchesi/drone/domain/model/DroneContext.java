package com.brianzolilecchesi.drone.domain.model;

public class DroneContext {
    private final DroneProperties properties;
    private DroneFlightMode flightMode;
    private int stepCounter;
    private Position currentDestination;
    private final double speed;

    public DroneContext(DroneProperties props, double speed) {
        this.properties = props;
        this.stepCounter = 0;
        this.flightMode = DroneFlightMode.NORMAL;
        this.currentDestination = new Position(props.getDestination(), 0);
        this.speed = speed;
    }

    public DroneProperties getDroneProperties() {
        return properties;
    }

    public Position getCurrentDestination() {
        return currentDestination;
    }

    public void setCurrentDestination(Position currentDestination) {
        this.currentDestination = currentDestination;
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

    public double getSpeed() {
        return speed;
    }
}
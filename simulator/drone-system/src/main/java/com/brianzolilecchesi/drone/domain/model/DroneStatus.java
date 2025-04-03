package com.brianzolilecchesi.drone.domain.model;

import java.util.List;

public class DroneStatus {
    private String droneId;
    private Position position;
    private double batteryLevel;
    private List<Position> flightPlan;
    private String log;

    public DroneStatus(String droneId, Position position, double batteryLevel, List<Position> flightPlan, String log) {
        this.droneId = droneId;
        this.position = position;
        this.batteryLevel = batteryLevel;
        this.flightPlan = flightPlan;
        this.log = log;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public List<Position> getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(List<Position> flightPlan) {
        this.flightPlan = flightPlan;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}

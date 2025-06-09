package com.brianzolilecchesi.drone.domain.model;

import java.util.List;

public class DroneStatus {
    private String droneId;
    private Position position;
    private double batteryLevel;
    private DroneFlightMode flightMode;
    private List<Position> flightPlan;
    private List<LogEntry> logs;

    public DroneStatus(String droneId, Position position, double batteryLevel, DroneFlightMode droneFlightMode, List<Position> flightPlan, List<LogEntry> logs) {
        this.droneId = droneId;
        this.position = position;
        this.batteryLevel = batteryLevel;
        this.flightMode = droneFlightMode;
        this.flightPlan = flightPlan;
        this.logs = logs;
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

    public DroneFlightMode getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(DroneFlightMode flightMode) {
        this.flightMode = flightMode;
    }

    public List<Position> getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(List<Position> flightPlan) {
        this.flightPlan = flightPlan;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
    }
}

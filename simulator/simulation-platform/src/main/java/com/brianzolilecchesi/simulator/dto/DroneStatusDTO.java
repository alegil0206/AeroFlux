package com.brianzolilecchesi.simulator.dto;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.LogEntry;
import com.brianzolilecchesi.drone.domain.model.Position;

public class DroneStatusDTO {
    private String droneId;
    private PositionDTO position;
    private double batteryLevel;
    private List<PositionDTO> flightPlan;
    private List<LogEntry> logs;

    public DroneStatusDTO(String droneId, PositionDTO position, double batteryLevel, List<PositionDTO> flightPlan, List<LogEntry> logs) {
        this.droneId = droneId;
        this.position = position;
        this.batteryLevel = batteryLevel;
        this.flightPlan = flightPlan;
        this.logs = logs;
    }

    public DroneStatusDTO(String droneId, Position position, double batteryLevel, List<Position> flightPlan, List<LogEntry> logs) {
        this.droneId = droneId;
        this.position = new PositionDTO(position);
        this.batteryLevel = batteryLevel;
        this.flightPlan = flightPlan.stream()
                .map(PositionDTO::new)
                .toList();
        this.logs = logs; 
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public List<PositionDTO> getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(List<PositionDTO> flightPlan) {
        this.flightPlan = flightPlan;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
    }
}

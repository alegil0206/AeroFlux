package com.brianzolilecchesi.simulator.model;

import java.util.List;
import java.util.Map;

import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.LogEntry;

public class SimulationHistory {
    
    private String simulationId;
    private String startTime;
    private List<LogEntry> logs;
    private Map<DroneProperties, List<DroneStatus>> droneStatusMap;

    public SimulationHistory(String simulationId, String startTime, List<LogEntry> logs, Map<DroneProperties, List<DroneStatus>> droneStatusMap) {
        this.simulationId = simulationId;
        this.startTime = startTime;
        this.logs = logs;
        this.droneStatusMap = droneStatusMap;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
    }

    public Map<DroneProperties, List<DroneStatus>> getDroneStatusMap() {
        return droneStatusMap;
    }

    public void setDroneStatusMap(Map<DroneProperties, List<DroneStatus>> droneStatusMap) {
        this.droneStatusMap = droneStatusMap;
    }

}

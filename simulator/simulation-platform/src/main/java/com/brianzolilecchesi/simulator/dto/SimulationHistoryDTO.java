package com.brianzolilecchesi.simulator.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationHistoryDTO extends SimulationDTO {

    private long duration;
    private int executionSpeed;
    private List<LogDTO> logs;
    private List<DroneHistoryDTO> drones;

    @JsonCreator
    public SimulationHistoryDTO(
            @JsonProperty("id") String simulationId,
            @JsonProperty("date") String simulationDate,
            @JsonProperty("duration") long duration,
            @JsonProperty("executionSpeed") int executionSpeed,
            @JsonProperty("logs") List<LogDTO> logs,
            @JsonProperty("drones") List<DroneHistoryDTO> drones) {
        super(simulationId, simulationDate);
        this.duration = duration;
        this.executionSpeed = executionSpeed;
        this.logs = logs;
        this.drones = drones;
    }

    public List<LogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<LogDTO> logs) {
        this.logs = logs;
    }

    public List<DroneHistoryDTO> getDrones() {
        return drones;
    }

    public void setDrones(List<DroneHistoryDTO> drones) {
        this.drones = drones;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getExecutionSpeed() {
        return executionSpeed;
    }

    public void setExecutionSpeed(int executionSpeed) {
        this.executionSpeed = executionSpeed;
    }
    
}

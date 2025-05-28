package com.brianzolilecchesi.simulator.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationDetailsDTO extends SimulationDTO {

    private List<LogDTO> logs;
    private List<DroneHistoryDTO> drones;

    @JsonCreator
    public SimulationDetailsDTO(
            @JsonProperty("id") String simulationId,
            @JsonProperty("date") String simulationDate,
            @JsonProperty("logs") List<LogDTO> logs,
            @JsonProperty("drones") List<DroneHistoryDTO> drones) {
        super(simulationId, simulationDate);
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
    
}

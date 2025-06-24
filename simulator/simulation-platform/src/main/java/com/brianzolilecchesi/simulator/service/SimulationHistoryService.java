package com.brianzolilecchesi.simulator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.LogEntry;
import com.brianzolilecchesi.simulator.dto.DroneHistoryDTO;
import com.brianzolilecchesi.simulator.dto.DronePropertiesDTO;
import com.brianzolilecchesi.simulator.dto.DroneStatusDTO;
import com.brianzolilecchesi.simulator.dto.LogDTO;
import com.brianzolilecchesi.simulator.dto.SimulationDTO;
import com.brianzolilecchesi.simulator.dto.SimulationHistoryDTO;
import com.brianzolilecchesi.simulator.model.SimulationHistory;

@Service
public class SimulationHistoryService {

    private final LogService logService;
    private Map<String, SimulationHistory> simulationHistory = new HashMap<>();
    private int simulationCounter = 0;
    private String prefix_id = "simulation_";

    public SimulationHistoryService(LogService logService) {
        this.logService = logService;
    }

    public synchronized List<SimulationDTO> getSimulations() {
        List<SimulationDTO> simulations = new ArrayList<>();
        for (SimulationHistory simulation : simulationHistory.values()) {
            simulations.add(new SimulationDTO(simulation.getSimulationId(), simulation.getStartTime()));
        }
        return simulations;
    }

    public synchronized SimulationHistoryDTO getSimulationHistoryById(String simulationId) {
        SimulationHistory simulationHistory = this.simulationHistory.get(simulationId);
        if (simulationHistory == null) {
            return null;
        }
      
        SimulationHistoryDTO simulationHistoryDTO = new SimulationHistoryDTO(
            simulationId,
            simulationHistory.getStartTime(),
            simulationHistory.getDuration(),
            simulationHistory.getExecutionSpeed(),
            simulationHistory.getLogs().stream().map(LogDTO::new).toList(),
            simulationHistory.getDroneStatusMap().entrySet().stream()
                .map(entry -> new DroneHistoryDTO(
                    new DronePropertiesDTO(entry.getKey()),
                    entry.getValue().stream().map(DroneStatusDTO::new).toList()))
                .toList()
        );

        return simulationHistoryDTO;
    }

    public synchronized void saveSimulationDetails(String startTime, long duration, int executionSpeed, Map<DroneProperties, List<DroneStatus>> droneStatusMap) {
        String simulationId = prefix_id + simulationCounter++;
        List<LogEntry> logs = logService.getAndClearLogEntries();
        SimulationHistory simulation = new SimulationHistory(simulationId, startTime, duration, executionSpeed, logs, droneStatusMap);
        simulationHistory.put(simulationId, simulation);
    }

}

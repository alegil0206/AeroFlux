package com.aeroflux.simulator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aeroflux.drone.domain.model.DroneProperties;
import com.aeroflux.drone.domain.model.DroneStatus;
import com.aeroflux.drone.domain.model.LogEntry;
import com.aeroflux.simulator.dto.DroneHistoryDTO;
import com.aeroflux.simulator.dto.DronePropertiesDTO;
import com.aeroflux.simulator.dto.DroneStatusDTO;
import com.aeroflux.simulator.dto.LogDTO;
import com.aeroflux.simulator.dto.SimulationDTO;
import com.aeroflux.simulator.dto.SimulationHistoryDTO;
import com.aeroflux.simulator.model.SimulationHistory;

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

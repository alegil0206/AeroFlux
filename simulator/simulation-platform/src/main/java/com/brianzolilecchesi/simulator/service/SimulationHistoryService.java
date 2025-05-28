package com.brianzolilecchesi.simulator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.brianzolilecchesi.simulator.dto.DroneHistoryDTO;
import com.brianzolilecchesi.simulator.dto.LogDTO;
import com.brianzolilecchesi.simulator.dto.SimulationDTO;
import com.brianzolilecchesi.simulator.dto.SimulationDetailsDTO;

@Service
public class SimulationHistoryService {

    private final LogService logService;
    private Map<SimulationDTO, SimulationDetailsDTO> simulationHistory = new HashMap<>();
    private int simulationCounter = 0;
    private String simulationId = "simulation-";

    public SimulationHistoryService(LogService logService) {
        this.logService = logService;
    }

    public Set<SimulationDTO> getSimulationHistory() {
        return simulationHistory.keySet();
    }

    public SimulationDetailsDTO getSimulationDetails(String simulationId) {
        for (Map.Entry<SimulationDTO, SimulationDetailsDTO> entry : simulationHistory.entrySet()) {
            if (entry.getKey().getId().equals(simulationId)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void saveSimulationDetails(String startTime, List<DroneHistoryDTO> droneHistoryList) {
        String simulationId = this.simulationId + simulationCounter++;
        SimulationDTO simulationDTO = new SimulationDTO(simulationId, startTime);
        List<LogDTO> logs = logService.getAndClearLogEntries();
        SimulationDetailsDTO simulationDetailsDTO = new SimulationDetailsDTO(simulationId, startTime, logs, droneHistoryList);
        simulationHistory.put(simulationDTO, simulationDetailsDTO);
    }

}

package com.aeroflux.simulator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.simulator.dto.SimulationDTO;
import com.aeroflux.simulator.dto.SimulationHistoryDTO;
import com.aeroflux.simulator.service.SimulationHistoryService;

@RestController
@RequestMapping(SimulationHistoryController.SIMULATION_HISTORY_BASE_URL)
public class SimulationHistoryController {
    
    public static final String SIMULATION_HISTORY_BASE_URL = "/simulation-history";

    private final SimulationHistoryService simulationHistoryService;
	
    @Autowired
    public SimulationHistoryController(SimulationHistoryService simulationHistoryService) {
        this.simulationHistoryService = simulationHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<SimulationDTO>> getSimulationHistory() {
        List<SimulationDTO> history = simulationHistoryService.getSimulations();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationHistoryDTO> getSimulationDetails(@PathVariable("id") final String id) {
        SimulationHistoryDTO simulationDetails = simulationHistoryService.getSimulationHistoryById(id);
        return ResponseEntity.ok(simulationDetails);
    }

}

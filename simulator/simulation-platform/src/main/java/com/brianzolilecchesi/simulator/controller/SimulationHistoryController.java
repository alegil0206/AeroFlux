package com.brianzolilecchesi.simulator.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.simulator.dto.SimulationDTO;
import com.brianzolilecchesi.simulator.dto.SimulationDetailsDTO;
import com.brianzolilecchesi.simulator.service.SimulationHistoryService;

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
    public ResponseEntity<Set<SimulationDTO>> getSimulationHistory() {
        Set<SimulationDTO> history = simulationHistoryService.getSimulationHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationDetailsDTO> getSimulationDetails(@PathVariable("id") final String id) {
        SimulationDetailsDTO simulationDetails = simulationHistoryService.getSimulationDetails(id);
        return ResponseEntity.ok(simulationDetails);
    }

}

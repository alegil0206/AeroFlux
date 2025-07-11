package com.aeroflux.simulator.controller;

import com.aeroflux.simulator.dto.SimulationStatusDTO;
import com.aeroflux.simulator.dto.StartCommandDTO;
import com.aeroflux.simulator.service.SimulationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SimulationController {
    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @MessageMapping("/start")
    @SendTo("/topic/status")
    public SimulationStatusDTO startSimulation(@Payload StartCommandDTO startCommand) {
        int speed = startCommand.getExecutionSpeed();
        return simulationService.start(speed);
    }

    @MessageMapping("/stop")
    @SendTo("/topic/status")
    public SimulationStatusDTO stopSimulation() {
        return simulationService.stop();
    }

    @MessageMapping("/pause")
    @SendTo("/topic/status")
    public SimulationStatusDTO pauseSimulation() {
        return simulationService.pause();
    }

    @MessageMapping("/resume")
    @SendTo("/topic/status")
    public SimulationStatusDTO resumeSimulation() {
        return simulationService.resume();
    }

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public SimulationStatusDTO getStatus() {
        return simulationService.getStatus();
    }
}

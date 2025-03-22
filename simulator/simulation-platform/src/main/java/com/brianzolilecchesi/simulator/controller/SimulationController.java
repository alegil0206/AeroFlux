package com.brianzolilecchesi.simulator.controller;

import com.brianzolilecchesi.simulator.service.SimulationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    public String startSimulation() {
        return simulationService.start();
    }

    @MessageMapping("/stop")
    @SendTo("/topic/status")
    public String stopSimulation() {
        return simulationService.stop();
    }

    @MessageMapping("/pause")
    @SendTo("/topic/status")
    public String pauseSimulation() {
        return simulationService.pause();
    }

    @MessageMapping("/resume")
    @SendTo("/topic/status")
    public String resumeSimulation() {
        return simulationService.resume();
    }

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public String getStatus() {
        return simulationService.getStatus();
    }
}

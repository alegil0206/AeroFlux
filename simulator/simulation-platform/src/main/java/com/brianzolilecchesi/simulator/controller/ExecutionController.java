package com.brianzolilecchesi.simulator.controller;

import com.brianzolilecchesi.simulator.service.ExecutionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ExecutionController {
    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @MessageMapping("/start")
    @SendTo("/topic/status")
    public String startSimulation() {
        return executionService.start();
    }

    @MessageMapping("/stop")
    @SendTo("/topic/status")
    public String stopSimulation() {
        return executionService.stop();
    }

    @MessageMapping("/pause")
    @SendTo("/topic/status")
    public String pauseSimulation() {
        return executionService.pause();
    }

    @MessageMapping("/resume")
    @SendTo("/topic/status")
    public String resumeSimulation() {
        return executionService.resume();
    }

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public String getStatus() {
        return executionService.getStatus();
    }
}

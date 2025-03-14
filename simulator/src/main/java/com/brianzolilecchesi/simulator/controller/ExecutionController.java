package com.brianzolilecchesi.simulator.controller;

import com.brianzolilecchesi.simulator.service.ExecutionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ExecutionController {
    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @MessageMapping("/start")
    public void startSimulation() {
        executionService.start();
    }

    @MessageMapping("/stop")
    public void stopSimulation() {
        executionService.stop();
    }

    @MessageMapping("/pause")
    public void pauseSimulation() {
        executionService.pause();
    }

    @MessageMapping("/resume")
    public void resumeSimulation() {
        executionService.resume();
    }
}

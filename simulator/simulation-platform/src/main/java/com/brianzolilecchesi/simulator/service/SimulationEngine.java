package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.model.SimulationStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulationEngine {
    private final SimulationStatus simulationStatus;
    private final LogService logService;

    public SimulationEngine(SimulationStatus simulationStatus, LogService logService) {
        this.simulationStatus = simulationStatus;
        this.logService = logService;
    }

    @Async()
    public void runSimulationLoop(List<DroneSystem> drones, long interval) {
        while (simulationStatus.isRunning()) {
            if (!simulationStatus.isPaused()) {
                for (DroneSystem drone : drones) {
                    drone.executeStep();
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        logService.sendLog("INFO", "Simulation task completed.");
    }
}

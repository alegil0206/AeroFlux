package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.model.SimulationStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {
    private final SimulationStatus simulationStatus;
    private final LogService logService;
    private final SimulationEngine simulationTaskService;
    private final DroneService droneService;
    private long interval = 3000; // Valore predefinito

    public SimulationService(SimulationStatus simulationStatus, LogService logService, 
            SimulationEngine simulationTaskService , DroneService droneService) {
        this.simulationStatus = simulationStatus;
        this.logService = logService;
        this.simulationTaskService = simulationTaskService;
        this.droneService = droneService;
    }

    public String start() {
        if (simulationStatus.isRunning() || simulationStatus.isPaused()) {
            stop();
        }

        simulationStatus.setRunning(true);
        simulationStatus.setPaused(false);

        List<DroneSystem> drones = droneService.createDrones();
        logService.sendLog("INFO", "Execution started with interval " + interval + "ms");

        // Starts the simulation in a separate thread
        simulationTaskService.runSimulationLoop(drones, interval);
        return simulationStatus.getStatus();
    }

    public String stop() {
        simulationStatus.setRunning(false);
        simulationStatus.setPaused(false);
        droneService.clearDrones();
        logService.sendLog("INFO", "Execution stopped");
        return simulationStatus.getStatus();
    }

    public String pause() {
        if (simulationStatus.isRunning() && !simulationStatus.isPaused()) {
            simulationStatus.setPaused(true);
            logService.sendLog("INFO", "Execution paused");
        }
        return simulationStatus.getStatus();
    }

    public String resume() {
        if (simulationStatus.isRunning() && simulationStatus.isPaused()) {
            simulationStatus.setPaused(false);
            logService.sendLog("INFO", "Execution resumed");
        }
        return simulationStatus.getStatus();
    }

    public String getStatus() {
        return simulationStatus.getStatus();
    }
}

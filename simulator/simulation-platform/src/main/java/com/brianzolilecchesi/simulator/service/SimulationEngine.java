package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.simulator.model.SimulationStatus;
import com.brianzolilecchesi.simulator.model.drone.SimulatedBattery;
import com.brianzolilecchesi.simulator.dto.DroneStatusDTO;

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
                    DroneStatus droneStatus = drone.executeStep();
                    if (droneStatus != null) {
                        DroneStatusDTO droneStatusDTO = new DroneStatusDTO(
                            droneStatus.getDroneId(),
                            droneStatus.getPosition(),
                            droneStatus.getBatteryLevel(),
                            droneStatus.getFlightPlan(),
                            droneStatus.getLog()
                        );
                        logService.sendDroneStatus(droneStatusDTO);
                        logService.sendLog(drone.getDroneProperties().getName(), droneStatus.getLog());
                        ((SimulatedBattery) drone.getHardwareAbstractionLayer().getBattery()).drainBattery(100);                        
                    }
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

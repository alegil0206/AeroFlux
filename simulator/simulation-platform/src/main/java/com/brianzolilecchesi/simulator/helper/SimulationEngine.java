package com.brianzolilecchesi.simulator.helper;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.simulator.model.Constants;
import com.brianzolilecchesi.simulator.model.SimulationStatus;
import com.brianzolilecchesi.simulator.model.drone.SimulatedBattery;
import com.brianzolilecchesi.simulator.model.drone.SimulatedRadio;
import com.brianzolilecchesi.simulator.service.LogService;
import com.brianzolilecchesi.simulator.service.SimulationHistoryService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimulationEngine {
    private final SimulationStatus simulationStatus;
    private final LogService logService;
    private final SimulationHistoryService simulationHistoryService;
    private final static long INTERVAL = 1000;

    public SimulationEngine(SimulationStatus simulationStatus, LogService logService, SimulationHistoryService simulationHistoryService) {
        this.simulationStatus = simulationStatus;
        this.logService = logService;
        this.simulationHistoryService = simulationHistoryService;
    }

    @Async
    public void runSimulationLoop(List<DroneSystem> drones) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String startTime = java.time.LocalDateTime.now().format(formatter);

        Map<DroneProperties, List<DroneStatus>> droneStatusMap = new HashMap<>();
        for (DroneSystem drone : drones) {
            DroneProperties droneProperties = drone.getDroneProperties();
            List<DroneStatus> droneStatusList = new ArrayList<>();
            droneStatusMap.put(droneProperties, droneStatusList);
        }

        long lastStepExecutionTime = 0;

        while (simulationStatus.getExecutionState() != SimulationStatus.ExecutionState.STOPPED) {
            if (simulationStatus.getExecutionState() == SimulationStatus.ExecutionState.RUNNING) {

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastStepExecutionTime;

                if (elapsedTime > INTERVAL / simulationStatus.getExecutionSpeed()) {

                    for (DroneSystem drone : drones) {
                        DroneStatus droneStatus = drone.executeStep();
                        if (droneStatus != null) {
                            logService.sendDroneStatus(droneStatus);
                            logService.registerLogEntries(droneStatus.getLogs());
                            ((SimulatedBattery) drone.getHardwareAbstractionLayer().getBattery()).drainBattery(100);
                            droneStatusMap.get(drone.getDroneProperties()).add(droneStatus);                  
                        }
                    }
                    SimulatedRadio.deliverMessages();
                    lastStepExecutionTime = currentTime;
                }
            }
        }

        logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_COMPLETED, "Simulation completed");
        simulationHistoryService.saveSimulationDetails(startTime, droneStatusMap);
    }
}

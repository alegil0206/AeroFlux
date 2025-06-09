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
import com.brianzolilecchesi.simulator.dto.DroneHistoryDTO;
import com.brianzolilecchesi.simulator.dto.DronePropertiesDTO;
import com.brianzolilecchesi.simulator.dto.DroneStatusDTO;

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
    private Map<DroneProperties, List<DroneStatusDTO>> droneStatusMap = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final static long INTERVAL = 1000;

    private long lastStepExecutionTime = 0;

    public SimulationEngine(SimulationStatus simulationStatus, LogService logService, SimulationHistoryService simulationHistoryService) {
        this.simulationStatus = simulationStatus;
        this.logService = logService;
        this.simulationHistoryService = simulationHistoryService;
    }

    @Async
    public void runSimulationLoop(List<DroneSystem> drones) {

        String startTime = java.time.LocalDateTime.now().format(formatter);

        for (DroneSystem drone : drones) {
            DroneProperties droneProperties = drone.getDroneProperties();
            List<DroneStatusDTO> droneStatusList = new ArrayList<>();
            droneStatusMap.put(droneProperties, droneStatusList);
        }

        while (simulationStatus.getExecutionState() != SimulationStatus.ExecutionState.STOPPED) {
            if (simulationStatus.getExecutionState() == SimulationStatus.ExecutionState.RUNNING) {

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastStepExecutionTime;

                if (elapsedTime > INTERVAL / simulationStatus.getExecutionSpeed()) {

                    for (DroneSystem drone : drones) {
                        DroneStatus droneStatus = drone.executeStep();
                        if (droneStatus != null) {
                            DroneStatusDTO droneStatusDTO = new DroneStatusDTO(
                                droneStatus.getDroneId(),
                                droneStatus.getPosition(),
                                droneStatus.getBatteryLevel(),
                                droneStatus.getFlightMode(),
                                droneStatus.getFlightPlan(),
                                droneStatus.getLogs()
                            );
                            logService.sendDroneStatus(droneStatusDTO);
                            logService.registerLogEntries(droneStatus.getLogs());
                            ((SimulatedBattery) drone.getHardwareAbstractionLayer().getBattery()).drainBattery(100);
                            droneStatusMap.get(drone.getDroneProperties()).add(droneStatusDTO);                  
                        }
                    }
                    SimulatedRadio.deliverMessages();
                    lastStepExecutionTime = currentTime;
                }
            }
        }

        List<DroneHistoryDTO> droneHistoryList = new ArrayList<>();

        for (DroneSystem drone : drones) {
            DroneProperties droneProperties = drone.getDroneProperties();
            DronePropertiesDTO dronePropertiesDTO = new DronePropertiesDTO(droneProperties);
            List<DroneStatusDTO> droneStatusList = droneStatusMap.get(droneProperties);
            new DroneHistoryDTO(dronePropertiesDTO, droneStatusList);
        }
        logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_COMPLETED, "Simulation completed");
        simulationHistoryService.saveSimulationDetails(startTime, droneHistoryList);
    }
}

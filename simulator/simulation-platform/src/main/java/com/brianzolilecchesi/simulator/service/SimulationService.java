package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.dto.SimulationStatusDTO;
import com.brianzolilecchesi.simulator.helper.DroneSystemFactory;
import com.brianzolilecchesi.simulator.helper.SimulationEngine;
import com.brianzolilecchesi.simulator.model.Constants;
import com.brianzolilecchesi.simulator.model.SimulationStatus;
import com.brianzolilecchesi.simulator.model.SimulationStatus.ExecutionState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {
    private final SimulationStatus simulationStatus;
    private final LogService logService;
    private final SimulationEngine simulationTaskService;
    private final DroneSystemFactory droneSystemFactory;

    public SimulationService(SimulationStatus simulationStatus, LogService logService, 
            SimulationEngine simulationTaskService, DroneSystemFactory droneSystemFactory) {
        this.simulationStatus = simulationStatus;
        this.logService = logService;
        this.simulationTaskService = simulationTaskService;
        this.droneSystemFactory = droneSystemFactory;
    }

    public SimulationStatusDTO start(int executionSpeed) {
        if (simulationStatus.getExecutionState() != ExecutionState.STOPPED) {
            stop();
        }
        
        logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_START, "Simulation started at speed " + simulationStatus.getExecutionSpeed() + "x");

        simulationStatus.setExecutionSpeed(executionSpeed);
        simulationStatus.setExecutionState(ExecutionState.RUNNING);

        List<DroneSystem> drones = droneSystemFactory.createDrones();

        simulationTaskService.runSimulationLoop(drones);
        return new SimulationStatusDTO(simulationStatus.getExecutionState(), simulationStatus.getExecutionSpeed());
    }

    public SimulationStatusDTO stop() {
        logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_STOP, "Simulation stopped");
        simulationStatus.setExecutionState(ExecutionState.STOPPED);
        return new SimulationStatusDTO(simulationStatus.getExecutionState(), simulationStatus.getExecutionSpeed());
    }

    public SimulationStatusDTO pause() {
        if (simulationStatus.getExecutionState() == ExecutionState.RUNNING) {
            logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_PAUSE, "Execution paused");
            simulationStatus.setExecutionState(ExecutionState.PAUSED);
        }
        return new SimulationStatusDTO(simulationStatus.getExecutionState(), simulationStatus.getExecutionSpeed());
    }

    public SimulationStatusDTO resume() {
        if (simulationStatus.getExecutionState() == ExecutionState.PAUSED) {
            logService.info(Constants.Service.SIMULATOR_SERVICE, Constants.Event.SIMULATION_RESUME, "Execution resumed");
            simulationStatus.setExecutionState(ExecutionState.RUNNING);
        }
        return new SimulationStatusDTO(simulationStatus.getExecutionState(), simulationStatus.getExecutionSpeed());
    }

    public SimulationStatusDTO getStatus() {
        return new SimulationStatusDTO(simulationStatus.getExecutionState(), simulationStatus.getExecutionSpeed());
    }
}

package com.brianzolilecchesi.simulator.dto;

import com.brianzolilecchesi.simulator.model.SimulationStatus.ExecutionState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationStatusDTO {
    
    private ExecutionState executionState;
    private int executionSpeed;

    @JsonCreator
    public SimulationStatusDTO(
        @JsonProperty("execution_state") ExecutionState executionState,
        @JsonProperty("execution_speed") int executionSpeed) {
            this.executionState = executionState;
            this.executionSpeed = executionSpeed;
    }

    @JsonProperty("execution_state")
    public ExecutionState getExecutionState() {
        return executionState;
    }

    public void setExecutionState(ExecutionState executionState) {
        this.executionState = executionState;
    }

    @JsonProperty("execution_speed")
    public int getExecutionSpeed() {
        return executionSpeed;
    }

    public void setExecutionSpeed(int executionSpeed) {
        this.executionSpeed = executionSpeed;
    }
}


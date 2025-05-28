package com.brianzolilecchesi.simulator.model;

import org.springframework.stereotype.Component;

@Component
public class SimulationStatus {
    private ExecutionState executionState = ExecutionState.STOPPED;
    private int executionSpeed = 1;

    public enum ExecutionState {
        RUNNING,
        PAUSED,
        STOPPED
    }

    public synchronized int getExecutionSpeed() {
        return executionSpeed;
    }

    public synchronized void setExecutionSpeed(int executionSpeed) {
        this.executionSpeed = executionSpeed;
    }

    public synchronized void setExecutionState(ExecutionState state) {
        this.executionState = state;
    }

    public synchronized ExecutionState getExecutionState() {
        return executionState;
    }
}

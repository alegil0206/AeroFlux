package com.aeroflux.simulator.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StartCommandDTO {

    private final int executionSpeed;

    @JsonCreator
    public StartCommandDTO(@JsonProperty("execution_speed") int executionSpeed) {
        this.executionSpeed = executionSpeed;
    }

    @JsonProperty("execution_speed")
    public int getExecutionSpeed() {
        return executionSpeed;
    }
}

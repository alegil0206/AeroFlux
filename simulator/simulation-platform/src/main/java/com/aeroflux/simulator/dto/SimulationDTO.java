package com.aeroflux.simulator.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationDTO {
    
    private String id;
    private String date;
    
    @JsonCreator
    public SimulationDTO(
        @JsonProperty("id") String simulationId,
        @JsonProperty("date") String simulationDate) {
            setId(simulationId);
            setDate(simulationDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String simulationId) {
        this.id = simulationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String simulationDate) {
        this.date = simulationDate;
    }

    @Override
    public String toString() {
        return "SimulationHistoryDTO{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationDTO)) return false;

        SimulationDTO that = (SimulationDTO) o;

        if (!id.equals(that.id)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

package com.aeroflux.drone.domain.dto;

import java.util.List;

import com.aeroflux.drone.domain.model.DroneFlightMode;
import com.aeroflux.drone.domain.model.Position;

public class RadioMessageDTO {
    private String droneId;
    private String operationCategory;
    private DroneFlightMode flightMode;
    private Position position;
    private List<Position> nextPositions;

    public RadioMessageDTO(String droneId, String operationCategory, DroneFlightMode flightMode, Position position, List<Position> nextPositions) {
        this.droneId = droneId;
        this.operationCategory = operationCategory;
        this.flightMode = flightMode;
        this.position = position;
        this.nextPositions = nextPositions;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public String getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(String operationCategory) {
        this.operationCategory = operationCategory;
    }

    public DroneFlightMode getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(DroneFlightMode flightMode) {
        this.flightMode = flightMode;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Position> getNextPositions() {
        return nextPositions;
    }

    public void setNextPositions(List<Position> nextPositions) {
        this.nextPositions = nextPositions;
    }

    @Override
    public String toString() {
        return "RadioMessage{" +
                "droneId='" + droneId + '\'' +
                ", flightMode=" + flightMode +
                ", position=" + position +
                ", nextPositions=" + nextPositions +
                '}';
    }
}

package com.aeroflux.drone.domain.model;

import java.util.List;

public class NearbyDroneStatus {
    private String droneId;
    private String operationCategory;
    private DroneFlightMode flightMode;
    private Position position;
    private List<Position> nextPositions;

    public NearbyDroneStatus(String droneId, String operationCategory, DroneFlightMode flightMode, Position position, List<Position> nextPositions) {
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

    public void setNextPosition(List<Position> nextPositions) {
        this.nextPositions = nextPositions;
    }

    @Override
    public String toString() {
        return "RadioMessage{" +
                "droneId='" + droneId + '\'' +
                ", flightMode=" + flightMode +
                ", position=" + position +
                ", nextPosition=" + nextPositions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NearbyDroneStatus)) return false;

        NearbyDroneStatus that = (NearbyDroneStatus) o;

        if (!droneId.equals(that.droneId)) return false;
        if (!operationCategory.equals(that.operationCategory)) return false;
        if (flightMode != that.flightMode) return false;
        if (!position.equals(that.position)) return false;
        return nextPositions.equals(that.nextPositions);
    }

    @Override
    public int hashCode() {
        int result = droneId.hashCode();
        result = 31 * result + operationCategory.hashCode();
        result = 31 * result + flightMode.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + nextPositions.hashCode();
        return result;
    }

}

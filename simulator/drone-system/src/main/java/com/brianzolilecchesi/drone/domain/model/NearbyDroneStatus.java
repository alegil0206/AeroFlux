package com.brianzolilecchesi.drone.domain.model;

public class NearbyDroneStatus {
    private String droneId;
    private DroneFlightMode flightMode;
    private Position position;
    private Position nextPosition;

    public NearbyDroneStatus(String droneId, DroneFlightMode flightMode, Position position, Position nextPosition) {
        this.droneId = droneId;
        this.flightMode = flightMode;
        this.position = position;
        this.nextPosition = nextPosition;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public DroneFlightMode isEmergency() {
        return flightMode;
    }

    public void setEmergency(DroneFlightMode flightMode) {
        this.flightMode = flightMode;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Position nextPosition) {
        this.nextPosition = nextPosition;
    }

    @Override
    public String toString() {
        return "RadioMessage{" +
                "droneId='" + droneId + '\'' +
                ", flightMode=" + flightMode +
                ", position=" + position +
                ", nextPosition=" + nextPosition +
                '}';
    }

}

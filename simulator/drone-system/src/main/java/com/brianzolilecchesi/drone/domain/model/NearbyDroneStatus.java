package com.brianzolilecchesi.drone.domain.model;

public class NearbyDroneStatus {
    private String droneId;
    private boolean isEmergency;
    private Position position;
    private Position nextPosition;

    public NearbyDroneStatus(String droneId, boolean isEmergency, Position position, Position nextPosition) {
        this.droneId = droneId;
        this.isEmergency = isEmergency;
        this.position = position;
        this.nextPosition = nextPosition;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
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
                ", isEmergency=" + isEmergency +
                ", position=" + position +
                ", nextPosition=" + nextPosition +
                '}';
    }

}

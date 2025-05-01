package com.brianzolilecchesi.drone.domain.dto;

import com.brianzolilecchesi.drone.domain.model.Position;

public class RadioMessageDTO {
    private String droneId;
    private boolean isEmergency;
    private Position position;
    private Position nexPosition;

    public RadioMessageDTO(String droneId, boolean isEmergency, Position position, Position nexPosition) {
        this.droneId = droneId;
        this.isEmergency = isEmergency;
        this.position = position;
        this.nexPosition = nexPosition;
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

    public Position getNexPosition() {
        return nexPosition;
    }

    public void setNexPosition(Position nexPosition) {
        this.nexPosition = nexPosition;
    }

    @Override
    public String toString() {
        return "RadioMessage{" +
                "droneId='" + droneId + '\'' +
                ", isEmergency=" + isEmergency +
                ", position=" + position +
                ", nexPosition=" + nexPosition +
                '}';
    }
}

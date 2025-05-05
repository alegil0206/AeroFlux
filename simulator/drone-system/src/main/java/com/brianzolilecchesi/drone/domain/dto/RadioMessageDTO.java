package com.brianzolilecchesi.drone.domain.dto;

import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.Position;

public class RadioMessageDTO {
    private String droneId;
    private DroneFlightMode flightMode;
    private Position position;
    private Position nexPosition;

    public RadioMessageDTO(String droneId, DroneFlightMode flightMode, Position position, Position nexPosition) {
        this.droneId = droneId;
        this.flightMode = flightMode;
        this.position = position;
        this.nexPosition = nexPosition;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
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
                ", flightMode=" + flightMode +
                ", position=" + position +
                ", nexPosition=" + nexPosition +
                '}';
    }
}

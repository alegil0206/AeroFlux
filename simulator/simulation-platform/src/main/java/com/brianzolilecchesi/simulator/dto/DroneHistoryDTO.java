package com.brianzolilecchesi.simulator.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DroneHistoryDTO {
    DronePropertiesDTO droneProperties;
    List<DroneStatusDTO> droneStatus;

    @JsonCreator
    public DroneHistoryDTO(
        @JsonProperty("drone_properties") DronePropertiesDTO droneProperties, 
        @JsonProperty("drone_status") List<DroneStatusDTO> droneStatus) {
            this.droneProperties = droneProperties;
            this.droneStatus = droneStatus;
    }

    public DronePropertiesDTO getDroneProperties() {
        return droneProperties;
    }

    public void setDroneProperties(DronePropertiesDTO droneProperties) {
        this.droneProperties = droneProperties;
    }

    public List<DroneStatusDTO> getPositions() {
        return droneStatus;
    }

    public void setPositions(List<DroneStatusDTO> droneStatus) {
        this.droneStatus = droneStatus;
    }
}

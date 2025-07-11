package com.aeroflux.drone.domain.dto;

import java.util.List;

public class RainCellDTO {
    private List<double[]> coordinates;

    public RainCellDTO(){}
    
    public RainCellDTO(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }
}

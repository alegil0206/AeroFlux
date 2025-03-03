package com.brianzolilecchesi.weather.dto;

import java.util.List;

public class RainCellDTO {
    private List<double[]> coordinates;

    public RainCellDTO(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }
}

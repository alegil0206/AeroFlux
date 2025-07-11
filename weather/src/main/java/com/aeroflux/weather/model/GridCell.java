package com.aeroflux.weather.model;

import java.util.List;

public class GridCell {
    private boolean raining;
    private List<double[]> coordinates;

    public GridCell(boolean raining, List<double[]> coordinates) {
        this.raining = raining;
        this.coordinates = coordinates;
    }

    public boolean isRaining() {
        return raining;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }
}
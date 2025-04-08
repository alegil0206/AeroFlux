package com.brianzolilecchesi.drone.domain.model;

import java.util.List;

public class WeatherData {
    private List<double[]> coordinates;

    public WeatherData(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WeatherData [");
        for (double[] coord : coordinates) {
            sb.append(String.format("(%.6f, %.6f)", coord[0], coord[1]));
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

}
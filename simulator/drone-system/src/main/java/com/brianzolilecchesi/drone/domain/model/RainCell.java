package com.brianzolilecchesi.drone.domain.model;

import java.util.List;

public class RainCell {
    private List<double[]> coordinates;

    public RainCell(List<double[]> coordinates) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RainCell rainCell = (RainCell) obj;
        if (coordinates.size() != rainCell.coordinates.size()) return false;
        for (int i = 0; i < coordinates.size(); i++) {
            double[] thisCoord = coordinates.get(i);
            double[] otherCoord = rainCell.coordinates.get(i);
            if (thisCoord.length != otherCoord.length) return false;
            for (int j = 0; j < thisCoord.length; j++) {
                if (Double.compare(thisCoord[j], otherCoord[j]) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

}
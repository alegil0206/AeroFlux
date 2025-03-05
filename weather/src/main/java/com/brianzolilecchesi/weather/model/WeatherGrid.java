package com.brianzolilecchesi.weather.model;

import java.util.List;
import java.util.ArrayList;

public class WeatherGrid {

    private static final double CELL_SIZE_METERS = 1000.0; // Dimensione della cella in metri
    private static final double EARTH_RADIUS = 6378137.0; // Raggio della Terra in metri

    public static double[] calculateNewCoordinates(double lat, double lon, double deltaX, double deltaY) {
        double latRad = Math.toRadians(lat);
        double newLat = lat + (deltaX / EARTH_RADIUS) * (180 / Math.PI);
        double newLon = lon + (deltaY / (EARTH_RADIUS * Math.cos(latRad))) * (180 / Math.PI);
        return new double[]{newLat, newLon};
    }

    private int gridSize;
    private GridCell[][] gridCells;
    private double centerLat, centerLon;

    public WeatherGrid(double centerLat, double centerLon, int gridSize) {
        this.gridSize = gridSize;
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.gridCells = new GridCell[gridSize][gridSize];
        generateGrid();
    }

    private void generateGrid() {
        double range = (double) gridSize / 2;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double latOffset = (i - range) * CELL_SIZE_METERS;
                double lonOffset = (j - range) * CELL_SIZE_METERS;
                
                List<double[]> coordinates = new ArrayList<>();
                coordinates.add(calculateNewCoordinates(centerLat, centerLon, latOffset, lonOffset));
                coordinates.add(calculateNewCoordinates(centerLat, centerLon, latOffset + CELL_SIZE_METERS, lonOffset));
                coordinates.add(calculateNewCoordinates(centerLat, centerLon, latOffset + CELL_SIZE_METERS, lonOffset + CELL_SIZE_METERS));
                coordinates.add(calculateNewCoordinates(centerLat, centerLon, latOffset, lonOffset + CELL_SIZE_METERS));
                coordinates.add(calculateNewCoordinates(centerLat, centerLon, latOffset, lonOffset)); 

                gridCells[i][j] = new GridCell(false, coordinates);
            }
        }
    }

    public GridCell[][] getGridCells() {
        return gridCells;
    }

    public GridCell getCell(int x, int y) {
        if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
            return gridCells[x][y];
        }
        return null;
    }
}
package com.brianzolilecchesi.weather.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

import com.brianzolilecchesi.weather.dto.CoordinatesDTO;
import com.brianzolilecchesi.weather.model.GridCell;

@Service
public class GridService {

    private static final int GRID_SIZE = 120;
    private static final double CELL_SIZE_METERS = 500.0; 
    private static final double EARTH_RADIUS = 6378137.0; 

    private GridCell[][] gridCells;
    private double centerLat = 45.476592;
    private double centerLon = 9.219752;

    public GridService() {
        this.gridCells = new GridCell[GRID_SIZE][GRID_SIZE];
        generateGrid();
    }

    private void generateGrid() {
        double range = (double) GRID_SIZE / 2;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
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

    public synchronized GridCell getCell(int x, int y) {
        if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
            return gridCells[x][y];
        }
        return null;
    }

    public synchronized void setCellRaining(int x, int y, boolean raining) {

        GridCell cell = getCell(x, y);
        if (cell != null)
            cell.setRaining(raining);
    }

    public synchronized void updateGridCenter(double newLat, double newLon) {
        this.centerLat = newLat;
        this.centerLon = newLon;
        this.gridCells = new GridCell[GRID_SIZE][GRID_SIZE];
        generateGrid();
    }

    public synchronized double getCenterLat() {
        return centerLat;
    }

    public synchronized double getCenterLon() {
        return centerLon;
    }

    private static double[] calculateNewCoordinates(double lat, double lon, double deltaX, double deltaY) {
        double latRad = Math.toRadians(lat);
        double newLat = lat + (deltaX / EARTH_RADIUS) * (180 / Math.PI);
        double newLon = lon + (deltaY / (EARTH_RADIUS * Math.cos(latRad))) * (180 / Math.PI);
        return new double[]{newLat, newLon};
    }

    public int getGridSize() {
        return GRID_SIZE;
    }

    public double getCellSizeMeters() {
        return CELL_SIZE_METERS;
    }

    public synchronized CoordinatesDTO getCenterCoordinatesDTO() {
        return new CoordinatesDTO(centerLat, centerLon);
    }

    public synchronized CoordinatesDTO setCenterCoordinates(CoordinatesDTO coordinates) {
        updateGridCenter(coordinates.getLatitude(), coordinates.getLongitude());
        return coordinates;
    }
}

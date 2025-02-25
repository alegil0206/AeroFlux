package com.brianzolilecchesi.weather.service;

import org.springframework.stereotype.Service;
import com.brianzolilecchesi.weather.dto.WeatherCellDTO;
import com.brianzolilecchesi.weather.model.GridCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WeatherService {
    private static final double CELL_SIZE_METERS = 40;
    private static final int GRID_SIZE = 1500;
    private static final double EARTH_RADIUS = 6378137; // metri

    private double latMin, lonMin, deltaLat, deltaLon;
    private Random random = new Random();

    public WeatherService() {
        double centerLat = 45.523901;
        double centerLon = 9.219752;

        this.deltaLat = CELL_SIZE_METERS / 111320;
        this.deltaLon = CELL_SIZE_METERS / (111320 * Math.cos(Math.toRadians(centerLat)));

        this.latMin = centerLat - (GRID_SIZE / 2.0) * deltaLat;
        this.lonMin = centerLon - (GRID_SIZE / 2.0) * deltaLon;
    }

    public List<WeatherCellDTO> getWeatherGrid() {
        List<WeatherCellDTO> grid = new ArrayList<>();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                double lat = latMin + i * deltaLat;
                double lon = lonMin + j * deltaLon;
                boolean isRaining = random.nextBoolean();

                grid.add(new WeatherCellDTO(i, j, lat, lon, isRaining));
            }
        }
        return grid;
    }
}
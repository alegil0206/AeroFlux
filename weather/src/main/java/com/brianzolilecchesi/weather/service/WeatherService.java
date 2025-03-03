package com.brianzolilecchesi.weather.service;

import com.brianzolilecchesi.weather.dto.RainCellDTO;
import com.brianzolilecchesi.weather.model.GridCell;
import com.brianzolilecchesi.weather.model.WeatherGrid;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherService {
    private static final int GRID_SIZE = 60; // 60 x 60 celle da 1000m -> 60km x 60km
    private static final double NEW_STORM_PROBABILITY = 0.05; // 10%
    private static final double PROPAGATION_PROBABILITY = 0.35; // 35%
    private static final double DISSIPATION_PROBABILITY = 0.35; // 35%

    private WeatherGrid weatherGrid;
    private Random random = new Random();

    public WeatherService() {
        weatherGrid = new WeatherGrid(45.476592, 9.219752, GRID_SIZE);
    }

    public List<RainCellDTO> getRainCells() {
        List<RainCellDTO> rainCells = new ArrayList<>();
        GridCell[][] gridCells = weatherGrid.getGridCells();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gridCells[i][j].isRaining()) {
                    rainCells.add(new RainCellDTO(gridCells[i][j].getCoordinates()));
                }
            }
        }
        return rainCells;
    }


    @Scheduled(fixedRate = 10000) // Ogni 10 secondi
    private void generateRain() {
        GridCell[][] gridCells = weatherGrid.getGridCells();
        double windX = 1.0; // Direzione del vento sulla X (ad esempio, da Ovest a Est)
        double windY = 0.5; // Direzione del vento sulla Y (ad esempio, da Nord a Sud)
    
        // Creazione di nuove celle temporalesche (10% di probabilità)
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!gridCells[i][j].isRaining() && random.nextDouble() < NEW_STORM_PROBABILITY) {
                    gridCells[i][j].setRaining(true);
                }
            }
        }
    
        // Propagazione & Dissipazione della pioggia
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                GridCell cell = gridCells[i][j];
    
                if (cell.isRaining()) {
                    // Dissipazione della pioggia (35%)
                    if (random.nextDouble() < DISSIPATION_PROBABILITY + windSpeedEffect()) {
                        cell.setRaining(false);
                    } else {
                        // Propagazione alle celle vicine in direzione del vento (35%)
                        propagateRain(gridCells, i, j, windX, windY); // Propaga nella direzione del vento
                        propagateRain(gridCells, i, j, -windX, -windY); // Propaga nell'opposta direzione
                    }
                }
            }
        }
    
        System.out.println("Aggiornamento pioggia completato.");
    }
    
    // Calcola l'effetto del vento sulla dissipazione della pioggia
    private double windSpeedEffect() {
        double windSpeed = 10.0; // Velocità del vento in km/h (puoi prendere questi dati da un'API)
        return Math.min(windSpeed / 100, 0.35); // Limitiamo l'effetto della dissipazione a un massimo del 35%
    }
    
    // Metodo helper per propagare la pioggia nella direzione del vento
    private void propagateRain(GridCell[][] gridCells, int x, int y, double windX, double windY) {
        int newX = (int) (x + windX);
        int newY = (int) (y + windY);
    
        if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE) {
            // Calcola la probabilità di propagazione in base alla direzione del vento
            double propagationProbability = random.nextDouble() + Math.abs(windX + windY) * 0.2;
            if (!gridCells[newX][newY].isRaining() && propagationProbability < PROPAGATION_PROBABILITY) {
                gridCells[newX][newY].setRaining(true);
            }
        }
    }

}

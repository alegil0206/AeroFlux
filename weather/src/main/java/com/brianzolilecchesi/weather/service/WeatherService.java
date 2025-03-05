package com.brianzolilecchesi.weather.service;

import com.brianzolilecchesi.weather.dto.RainCellDTO;
import com.brianzolilecchesi.weather.dto.WeatherConfigDTO;
import com.brianzolilecchesi.weather.model.GridCell;
import com.brianzolilecchesi.weather.model.WeatherGrid;
import com.brianzolilecchesi.weather.model.RainCluster;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherService {
    private static final int GRID_SIZE = 60;
    private WeatherGrid weatherGrid;
    private List<RainCluster> rainClusters = new ArrayList<>();
    private Random random = new Random();

    private double windDirection = 0;
    private double windIntensity = 1;
    private int minClusters = 1;
    private int maxClusters = 3;
    private int maxClusterSize = 3;

    public WeatherService() {
        weatherGrid = new WeatherGrid(45.476592, 9.219752, GRID_SIZE);
        initializeClusters();
    }

    public List<RainCellDTO> getRainCells() {
        List<RainCellDTO> rainCells = new ArrayList<>();
        for(int i=0; i<GRID_SIZE; i++) {
            for(int j=0; j<GRID_SIZE; j++) {
                GridCell cell = weatherGrid.getCell(i, j);
                if(cell.isRaining()) {
                    rainCells.add(new RainCellDTO(cell.getCoordinates()));
                }
            }
        }
        return rainCells;
    }

    public WeatherConfigDTO updateConfig(WeatherConfigDTO config) {
        windDirection = config.getWindDirection();
        windIntensity = config.getWindIntensity();
        minClusters = config.getMinClusters();
        maxClusters = config.getMaxClusters();
        maxClusterSize = config.getMaxClusterSize();
        return getConfig();
    }

    public WeatherConfigDTO getConfig() {
        WeatherConfigDTO config = new WeatherConfigDTO();
        config.setWindDirection(windDirection);
        config.setWindIntensity(windIntensity);
        config.setMinClusters(minClusters);
        config.setMaxClusters(maxClusters);
        config.setMaxClusterSize(maxClusterSize);
        return config;
    }


    private void initializeClusters() {
        int newClusters = random.nextInt(maxClusters - minClusters + 1) + minClusters - rainClusters.size();
        for (int i = 0; i < newClusters; i++) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            rainClusters.add(new RainCluster(x, y, maxClusterSize));
        }
    }

    @Scheduled(fixedRate = 5000)
    private void updateRain() {

        clearClusters();

        Iterator<RainCluster> iterator = rainClusters.iterator();
        while (iterator.hasNext()) {
            RainCluster cluster = iterator.next();
        
            cluster.move(windDirection, windIntensity);
        
            int cx = cluster.getCenterX();
            int cy = cluster.getCenterY();
        
            if (cx < -cluster.getMaxSize() || cx > GRID_SIZE + cluster.getMaxSize() ||
                cy < -cluster.getMaxSize() || cy > GRID_SIZE + cluster.getMaxSize()) {
                iterator.remove(); // Safe removal
                continue;
            }

            int incrementSize = 0;
            if(cluster.getActualSize() == cluster.getMaxSize()) {
                incrementSize = random.nextBoolean() ? -1 : 0;
            } else {
                incrementSize = random.nextInt(3) - 1;
            }
            cluster.setActualSize(cluster.getActualSize() + incrementSize); 

            if(cluster.getActualSize() == 0) {
                iterator.remove();
                continue;
            }

            for (int i = -cluster.getActualSize(); i < cluster.getActualSize(); i++) {
                for (int j = -cluster.getActualSize(); j < cluster.getActualSize(); j++) {
                    int nx = cx + i;
                    int ny = cy + j;
                    if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                        double distance = Math.sqrt(i * i + j * j);
                        double maxDistance = cluster.getActualSize();
                        // Interpolazione lineare della probabilità (dal 100% al 50%)
                        double rainChanceThreshold = 1.0 - (0.5 * (distance / maxDistance));
                        if (random.nextDouble() <= rainChanceThreshold) {
                            weatherGrid.getCell(nx, ny).setRaining(true);
                        }
                    }
                }
            }
        }
        
        initializeClusters();

    }

    private void clearClusters() {
        Iterator<RainCluster> iterator = rainClusters.iterator();
        while (iterator.hasNext()) {
            RainCluster cluster = iterator.next();
            for (int i = -cluster.getActualSize(); i < cluster.getActualSize(); i++) {
                for (int j = -cluster.getActualSize(); j < cluster.getActualSize(); j++) {
                    int nx = cluster.getCenterX() + i;
                    int ny = cluster.getCenterY() + j;
                    if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                        weatherGrid.getCell(nx, ny).setRaining(false);
                    }
                }
            }
        }
    }
}


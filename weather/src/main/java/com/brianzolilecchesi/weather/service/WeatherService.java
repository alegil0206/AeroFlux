package com.brianzolilecchesi.weather.service;

import com.brianzolilecchesi.weather.dto.RainCellDTO;
import com.brianzolilecchesi.weather.dto.WeatherConfigDTO;
import com.brianzolilecchesi.weather.model.GridCell;
import com.brianzolilecchesi.weather.model.RainCluster;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherService {

    private final GridService gridService;
    
    private List<RainCluster> rainClusters = new ArrayList<>();
    private Random random = new Random();

    private double windDirection;
    private double windIntensity; // km/h
    private int minClusters;
    private int maxClusters;
    private int maxClusterRadius;
    private double maxClusterRadiusKm; 

    public WeatherService(GridService gridService) {
        this.gridService = gridService;
        this.windDirection = 0.0;
        this.windIntensity = 30.0; // km/h
        this.minClusters = 2;
        this.maxClusters = 4;
        this.maxClusterRadiusKm = 3.0; // km
        this.maxClusterRadius = (int) (maxClusterRadiusKm / (gridService.getCellSizeMeters() / 1000.0));
        initializeClusters(false);
    }

    public List<RainCellDTO> getRainCells() {
        List<RainCellDTO> rainCells = new ArrayList<>();
        for(int i=0; i < gridService.getGridSize(); i++) {
            for(int j=0; j<gridService.getGridSize(); j++) {
                GridCell cell = gridService.getCell(i, j);
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
        maxClusterRadiusKm = config.getMaxClusterRadius();
        maxClusterRadius = (int) (maxClusterRadiusKm / (gridService.getCellSizeMeters() / 1000.0)); // Convert km to cells
        return getConfig();
    }

    public WeatherConfigDTO getConfig() {
        WeatherConfigDTO config = new WeatherConfigDTO();
        config.setWindDirection(windDirection);
        config.setWindIntensity(windIntensity);
        config.setMinClusters(minClusters);
        config.setMaxClusters(maxClusters);
        config.setMaxClusterRadius(maxClusterRadiusKm);
        return config;
    }

    private void initializeClusters(boolean considerWind) {
        int newClusters = random.nextInt(maxClusters - minClusters + 1) + minClusters - rainClusters.size();
        for (int i = 0; i < newClusters; i++) {
            int x = 0, y = 0;
            int gridSize = gridService.getGridSize();

            if (considerWind) {    
                double adjustedDirection = (windDirection + 180.0) % 360.0;
                double radians = Math.toRadians(adjustedDirection);
                double dx = Math.cos(radians);
                double dy = Math.sin(radians);
                if (Math.abs(dx) > Math.abs(dy)) {
                    x = dx > 0 ? -maxClusterRadius : gridSize + maxClusterRadius;
                    y = random.nextInt(gridSize);
                } else {
                    x = random.nextInt(gridSize);
                    y = dy > 0 ? -maxClusterRadius : gridSize + maxClusterRadius;
                }
            } else {
                x = random.nextInt(gridSize);
                y = random.nextInt(gridSize);
            }
            rainClusters.add(new RainCluster(x, y, maxClusterRadius));
        }
    }

    @Scheduled(fixedRate = 60000)
    private void updateRain() {

        clearClusters();

        Iterator<RainCluster> iterator = rainClusters.iterator();
        while (iterator.hasNext()) {
            RainCluster cluster = iterator.next();
        
            cluster.move(windDirection, windIntensity, 60.0, gridService.getCellSizeMeters() / 1000.0);
        
            int cx = cluster.getCenterX();
            int cy = cluster.getCenterY();
        
            if (cx < -cluster.getMaxRadius() * 2 || cx > gridService.getGridSize() + cluster.getMaxRadius() * 2 ||
                cy < -cluster.getMaxRadius() * 2 || cy > gridService.getGridSize() + cluster.getMaxRadius() * 2) {
                iterator.remove(); // Safe removal
                continue;
            }

            double increaseProbability = random.nextDouble();
            if (increaseProbability < 0.3) {
                cluster.setActualRadius(Math.min(cluster.getActualRadius() + 1, cluster.getMaxRadius()));
            } else if (increaseProbability < 0.4) {
                cluster.setActualRadius(Math.max(cluster.getActualRadius() - 1, 0));
            }

            if(cluster.getActualRadius() < 0) {
                iterator.remove();
                continue;
            }

            for (int i = -cluster.getActualRadius(); i <= cluster.getActualRadius(); i++) {
                for (int j = -cluster.getActualRadius(); j <= cluster.getActualRadius(); j++) {
                    int nx = cx + i;
                    int ny = cy + j;
                    if (nx >= 0 && nx < gridService.getGridSize() && ny >= 0 && ny < gridService.getGridSize()) {
                        double distance = Math.sqrt(i * i + j * j);
                        double maxDistance = cluster.getActualRadius();
                        double rainChanceThreshold = 1.0 - (0.7 * (distance / maxDistance));
                        if (random.nextDouble() <= rainChanceThreshold) {
                            gridService.setCellRaining(nx, ny, true);
                        }
                    }
                }
            }
        }
        
        initializeClusters(true);

    }

    private void clearClusters() {
        Iterator<RainCluster> iterator = rainClusters.iterator();
        while (iterator.hasNext()) {
            RainCluster cluster = iterator.next();
            for (int i = -cluster.getActualRadius(); i <= cluster.getActualRadius(); i++) {
                for (int j = -cluster.getActualRadius(); j <= cluster.getActualRadius(); j++) {
                    int nx = cluster.getCenterX() + i;
                    int ny = cluster.getCenterY() + j;
                    if (nx >= 0 && nx < gridService.getGridSize() && ny >= 0 && ny < gridService.getGridSize()) {
                        gridService.setCellRaining(nx, ny, false);
                    }
                }
            }
        }
    }
}


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
        this.windIntensity = 30.0;
        this.minClusters = 2;
        this.maxClusters = 4;
        this.maxClusterRadiusKm = 3.0;
        this.maxClusterRadius = computeMaxRadiusInCells();
        synchronized (rainClusters) {
            removeRainToClusters();
            initializeClusters(false);
            updateRain();
        }
    }

    public List<RainCellDTO> getRainCells() {
        List<RainCellDTO> rainCells = new ArrayList<>();
        for (int i = 0; i < gridService.getGridSize(); i++) {
            for (int j = 0; j < gridService.getGridSize(); j++) {
                GridCell cell = gridService.getCell(i, j);
                if (cell.isRaining()) {
                    rainCells.add(new RainCellDTO(cell.getCoordinates()));
                }
            }
        }
        return rainCells;
    }

    public WeatherConfigDTO updateConfig(WeatherConfigDTO config) {
        this.windDirection = config.getWindDirection();
        this.windIntensity = config.getWindIntensity();
        this.minClusters = config.getMinClusters();
        this.maxClusters = config.getMaxClusters();
        this.maxClusterRadiusKm = config.getMaxClusterRadius();
        this.maxClusterRadius = computeMaxRadiusInCells();

        synchronized (rainClusters) {
            removeRainToClusters();
            initializeClusters(false);
            updateRain();
        }

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

    private int computeMaxRadiusInCells() {
        return (int) (maxClusterRadiusKm / (gridService.getCellSizeMeters() / 1000.0));
    }

    private void initializeClusters(boolean considerWind) {
        limitClusterSize();
        int newClusters = random.nextInt(maxClusters - minClusters + 1) + minClusters - rainClusters.size();
        spawnNewClusters(newClusters, considerWind);
    }

    private void limitClusterSize() {
        if (rainClusters.size() > maxClusters) {
            int start = rainClusters.size() - maxClusters;
            List<RainCluster> limited = new ArrayList<>(rainClusters.subList(start, rainClusters.size()));
            rainClusters.clear();
            rainClusters.addAll(limited);
        }
    }

    private void spawnNewClusters(int count, boolean considerWind) {
        int gridSize = gridService.getGridSize();
        for (int i = 0; i < count; i++) {
            int x, y;
            if (considerWind) {
                double radians = Math.toRadians((windDirection + 180.0) % 360.0);
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
    private void updateWeather() {
        synchronized (rainClusters) {
            removeRainToClusters();
            initializeClusters(true);
            updateRain();
        }
    }

    private void updateRain() {
        Iterator<RainCluster> iterator = rainClusters.iterator();

        while (iterator.hasNext()) {
            RainCluster cluster = iterator.next();

            cluster.move(windDirection, windIntensity, 60.0, gridService.getCellSizeMeters() / 1000.0);

            int cx = cluster.getCenterX();
            int cy = cluster.getCenterY();
            int gridSize = gridService.getGridSize();

            if (cx < -cluster.getMaxRadius() * 2 || cx > gridSize + cluster.getMaxRadius() * 2 ||
                cy < -cluster.getMaxRadius() * 2 || cy > gridSize + cluster.getMaxRadius() * 2) {
                iterator.remove();
                continue;
            }

            double changeChance = random.nextDouble();
            if (changeChance < 0.3) {
                cluster.setActualRadius(Math.min(cluster.getActualRadius() + 1, cluster.getMaxRadius()));
            } else if (changeChance < 0.4) {
                cluster.setActualRadius(Math.max(cluster.getActualRadius() - 1, 0));
            }

            if (cluster.getActualRadius() <= 0) {
                iterator.remove();
                continue;
            }

            applyRainToCluster(cluster, gridSize);
        }
    }

    private void applyRainToCluster(RainCluster cluster, int gridSize) {
        int cx = cluster.getCenterX();
        int cy = cluster.getCenterY();
        int radius = cluster.getActualRadius();

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int nx = cx + i;
                int ny = cy + j;

                if (nx >= 0 && nx < gridSize && ny >= 0 && ny < gridSize) {
                    double distance = Math.sqrt(i * i + j * j);
                    double rainChanceThreshold = 1.0 - (0.7 * (distance / radius));
                    if (random.nextDouble() <= rainChanceThreshold) {
                        gridService.setCellRaining(nx, ny, true);
                    }
                }
            }
        }
    }

    private void removeRainToClusters() {
        int gridSize = gridService.getGridSize();
        for (RainCluster cluster : rainClusters) {
            int cx = cluster.getCenterX();
            int cy = cluster.getCenterY();
            int radius = cluster.getActualRadius();
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int nx = cx + i;
                    int ny = cy + j;
                    if (nx >= 0 && nx < gridSize && ny >= 0 && ny < gridSize) {
                        gridService.setCellRaining(nx, ny, false);
                    }
                }
            }
        }
    }

}


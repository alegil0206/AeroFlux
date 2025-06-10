package com.brianzolilecchesi.weather.dto;

public class WeatherConfigDTO {
    private double windDirection;
    private double windIntensity;
    private int minClusters;
    private int maxClusters;
    private double maxClusterRadius;

    public double getWindDirection() { return windDirection;}
    public void setWindDirection(double windDirection) { this.windDirection = windDirection; }
    public double getWindIntensity() { return windIntensity; }
    public void setWindIntensity(double windIntensity) { this.windIntensity = windIntensity; }
    public int getMinClusters() { return minClusters; }
    public void setMinClusters(int minClusters) { this.minClusters = minClusters; }
    public int getMaxClusters() { return maxClusters; }
    public void setMaxClusters(int maxClusters) { this.maxClusters = maxClusters; }
    public double getMaxClusterRadius() { return maxClusterRadius; }
    public void setMaxClusterRadius(double maxClusterRadius) { this.maxClusterRadius = maxClusterRadius; }
}

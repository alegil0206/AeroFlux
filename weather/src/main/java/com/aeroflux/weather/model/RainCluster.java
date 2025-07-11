package com.aeroflux.weather.model;

public class RainCluster {
    private int centerX;
    private int centerY;
    private int maxRadius;
    private int actualRadius;
    private double driftX;
    private double driftY;

    public RainCluster(int x, int y, int maxSize) {
        this.centerX = x;
        this.centerY = y;
        this.maxRadius = maxSize;
        this.actualRadius = maxSize;
        this.driftX = 0;
        this.driftY = 0;
    }

    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    public int getMaxRadius() { return maxRadius; }
    public int getActualRadius() { return actualRadius; }

    public void setActualRadius(int actualSize) {
        this.actualRadius = actualSize;
    }

    public void move(double windDirectionDeg, double windSpeedKmh, double intervalSeconds, double cellSizeKm) {
        double kmPerUpdate = (windSpeedKmh / 3600) * intervalSeconds / cellSizeKm;

        double adjustedDirection = (windDirectionDeg + 180) % 360;
        double radians = Math.toRadians(adjustedDirection);

        driftX += Math.cos(radians) * kmPerUpdate;
        driftY += Math.sin(radians) * kmPerUpdate;

        int dx = (int) Math.floor(driftX);
        int dy = (int) Math.floor(driftY);

        centerX += dx;
        centerY += dy;

        driftX -= dx; 
        driftY -= dy;
    }
}
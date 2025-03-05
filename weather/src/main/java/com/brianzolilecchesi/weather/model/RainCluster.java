package com.brianzolilecchesi.weather.model;

public class RainCluster {
    private int centerX;
    private int centerY;
    private int maxSize;
    private int actualSize;

    public RainCluster(int x, int y, int maxSize) {
        this.centerX = x;
        this.centerY = y;
        this.maxSize = maxSize;
        this.actualSize = 1;
    }

    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    public int getMaxSize() { return maxSize; }
    public int getActualSize() { return actualSize; }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }

    public void move(double windDirection, double windIntensity) {
        double radians = Math.toRadians(windDirection);
        centerX += Math.cos(radians) * windIntensity;
        centerY += Math.sin(radians) * windIntensity;
    }
}

package com.brianzolilecchesi.simulator.model;

public class UserCoordinates {
    private double centerLat;
    private double centerLon;

    public UserCoordinates(double centerLat, double centerLon) {
        this.centerLat = centerLat;
        this.centerLon = centerLon;
    }

    public double getCenterLat() { return centerLat; }
    public void setCenterLat(double centerLat) { this.centerLat = centerLat; }

    public double getCenterLon() { return centerLon; }
    public void setCenterLon(double centerLon) { this.centerLon = centerLon; }
}

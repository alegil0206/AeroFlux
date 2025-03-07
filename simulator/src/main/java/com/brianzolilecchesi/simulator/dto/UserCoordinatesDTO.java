package com.brianzolilecchesi.simulator.dto;

public class UserCoordinatesDTO {
    private double latitude;
    private double longitude;

    public UserCoordinatesDTO() {}

    public UserCoordinatesDTO(double centerLat, double centerLon) {
        this.latitude = centerLat;
        this.longitude = centerLon;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double centerLat) { this.latitude = centerLat; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double centerLon) { this.longitude = centerLon; }
}

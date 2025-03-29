package com.brianzolilecchesi.drone.domain.model;

public class Coordinates {
    private double latitude;
    private double longitude;
    private static final double EARTH_RADIUS = 6378137.0; 
    
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distanceTo(Coordinates coordinates) {
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(coordinates.getLatitude());

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(this.longitude - coordinates.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}

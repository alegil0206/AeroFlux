package com.brianzolilecchesi.drone.domain.dto;
import java.util.List;

public class GeoZoneDTO {
 private String id;
    private String name;
    private String type;
    private String category;
    private String status;
    private String altitude_level;
    private double altitude;

    
    private Double latitude;
    private Double longitude;
    private Double radius;

    private List<List<Double>> coordinates;
    
    public GeoZoneDTO(){}

    public GeoZoneDTO(String id, String name, String type, String category, String status, String altitude_level, double altitude,
                      Double latitude, Double longitude, Double radius, List<List<Double>> coordinates) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.altitude_level = altitude_level;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.coordinates = coordinates;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAltitude_level() { return altitude_level; }
    public void setAltitude_level(String altitude_level) { this.altitude_level = altitude_level; }

    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getRadius() { return radius; }
    public void setRadius(Double radius) { this.radius = radius; }

    public List<List<Double>> getCoordinates() { return coordinates; }
    public void setCoordinates(List<List<Double>> coordinates) { this.coordinates = coordinates; }
}
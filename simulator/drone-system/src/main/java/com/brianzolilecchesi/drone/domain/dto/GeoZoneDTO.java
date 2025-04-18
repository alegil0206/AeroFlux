package com.brianzolilecchesi.drone.domain.dto;
import java.util.List;

public class GeoZoneDTO {
 private String id;
    private String name;
    private String type;
    private String category;
    private String status;
    private String altitude_level_limit_inferior;
    private Double altitude_limit_inferior;
    private String altitude_level_limit_superior;
    private Double altitude_limit_superior;
    private Double latitude;
    private Double longitude;
    private Double radius;

    private List<List<Double>> coordinates;
    
    public GeoZoneDTO(){}

    public GeoZoneDTO(String id, String name, String type, String category, String status, String altitude_level_limit_inferior, Double altitude_limit_inferior,
            String altitude_level_limit_superior, Double altitude_limit_superior, Double latitude, Double longitude, Double radius, List<List<Double>> coordinates) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.altitude_level_limit_inferior = altitude_level_limit_inferior;
        this.altitude_limit_inferior = altitude_limit_inferior;
        this.altitude_level_limit_superior = altitude_level_limit_superior;
        this.altitude_limit_superior = altitude_limit_superior;
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

    public String getAltitude_level_limit_inferior() { return altitude_level_limit_inferior; }
    public void setAltitude_level_limit_inferior(String altitude_level) { this.altitude_level_limit_inferior = altitude_level; }

    public Double getAltitude_limit_inferior() { return altitude_limit_inferior; }
    public void setAltitude_limit_inferior(Double altitude) { this.altitude_limit_inferior = altitude; }

    public String getAltitude_level_limit_superior() { return altitude_level_limit_superior; }
    public void setAltitude_level_limit_superior(String altitude_level) { this.altitude_level_limit_superior = altitude_level; }

    public Double getAltitude_limit_superior() { return altitude_limit_superior; }
    public void setAltitude_limit_superior(Double altitude) { this.altitude_limit_superior = altitude; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getRadius() { return radius; }
    public void setRadius(Double radius) { this.radius = radius; }

    public List<List<Double>> getCoordinates() { return coordinates; }
    public void setCoordinates(List<List<Double>> coordinates) { this.coordinates = coordinates; }
}
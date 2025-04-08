package com.brianzolilecchesi.drone.domain.model;
import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;

public class GeoZone {
    private String id;
    private String name;
    private String type;
    private String category;
    private String status;
    private String altitudeLevel;
    private Double altitude;

    // Attributi per geozone circolari
    private Double latitude;
    private Double longitude;
    private Double radius;

    // Attributi per geozone poligonali
    private List<Coordinate> coordinates;


    public GeoZone(String id, String name, String type, String category, String status,
                   String altitudeLevel, Double altitude, Double latitude, Double longitude,
                   Double radius, List<Coordinate> coordinates) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.altitudeLevel = altitudeLevel;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.coordinates = coordinates;
    }
    
     public GeoZone(GeoZoneDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.type = dto.getType();
        this.category = dto.getCategory();
        this.status = dto.getStatus();
        this.altitudeLevel = dto.getAltitude_level();
        this.altitude = dto.getAltitude();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.radius = dto.getRadius();
        this.coordinates = new ArrayList<>();
    
        if (dto.getCoordinates() != null) {
            for (List<Double> pair : dto.getCoordinates()) {
                if (pair != null && pair.size() >= 2 && pair.get(0) != null && pair.get(1) != null) {
                    this.coordinates.add(new Coordinate(pair.get(1), pair.get(0)));
                }
            }
        }
    }
    public GeoZone() {
    	
    }

    // Getters e Setters
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

    public String getAltitudeLevel() { return altitudeLevel; }
    public void setAltitudeLevel(String altitudeLevel) { this.altitudeLevel = altitudeLevel; }

    public Double getAltitude() { return altitude; }
    public void setAltitude(Double altitude) { this.altitude = altitude; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getRadius() { return radius; }
    public void setRadius(Double radius) { this.radius = radius; }

    public List<Coordinate> getCoordinates() { return coordinates; }
    public void setCoordinates(List<Coordinate> coordinates) { this.coordinates = coordinates; }

    

@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Geozone ID: ").append(id)
      .append("\nName: ").append(name)
      .append("\nType: ").append(type)
      .append("\nStatus: ").append(status)
      .append("\nAltitude: ").append(altitude);

    if ("CIRCULAR".equalsIgnoreCase(type)) {
        sb.append("\nLatitude: ").append(latitude)
          .append("\nLongitude: ").append(longitude)
          .append("\nRadius: ").append(radius);
    }

    if (coordinates != null && !coordinates.isEmpty()) {
        sb.append("\nCoordinates: ").append(coordinates);
    }

    sb.append("\n---------------------------");
    return sb.toString();
}

}

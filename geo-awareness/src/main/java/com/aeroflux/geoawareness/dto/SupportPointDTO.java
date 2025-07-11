package com.aeroflux.geoawareness.dto;

import com.aeroflux.geoawareness.model.persistency.supportPoint.SupportPoint;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportPointDTO {
    
    private String id;
    private String name;
    private double longitude;
    private double latitude;

    @JsonCreator
    public SupportPointDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name, 
        @JsonProperty("longitude") double longitude,
        @JsonProperty("latitude") double latitude
    ) {
        setId(id);
        setName(name);
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public SupportPointDTO(SupportPoint supportPoint) {
        setId(supportPoint.getId());
        setName(supportPoint.getName());
        setLongitude(supportPoint.getCoordinates().getX());
        setLatitude(supportPoint.getCoordinates().getY());
    }

    public SupportPointDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SupportPointDTO that = (SupportPointDTO) obj;
        return Double.compare(that.longitude, longitude) == 0 &&
               Double.compare(that.latitude, latitude) == 0 &&
               id.equals(that.id) && 
               name.equals(that.name);
    }

    @Override
    public String toString() {
        return String.format(
            "SupportPointDTO[id=%s, name=%s, longitude=%f, latitude=%f]",
            getId(), 
            getName(), 
            getLongitude(), 
            getLatitude()
        );
    }

}

package com.brianzolilecchesi.drone.domain.dto;

public class SupportPointDTO {
    
    private String id;
    private String name;
    private Double longitude;
    private Double latitude;

    public SupportPointDTO(
        String id,
        String name, 
        Double longitude,
        Double latitude
    ) {
        setId(id);
        setName(name);
        setLongitude(longitude);
        setLatitude(latitude);
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
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

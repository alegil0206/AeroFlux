package com.brianzolilecchesi.drone.domain.dto;

public class AuthorizationRequestDTO {
    private String drone_id;
    private String geozone_id;
    private String duration_type;
    private Integer duration;

    public AuthorizationRequestDTO() {
    }

    public AuthorizationRequestDTO(String drone_id, String geozone_id, String duration_type, Integer duration) {
        this.drone_id = drone_id;
        this.geozone_id = geozone_id;
        this.duration_type = duration_type;
        this.duration = duration;
    }

    public String getDrone_id() {
        return drone_id;
    }

    public void setDrone_id(String drone_id) {
        this.drone_id = drone_id;
    }

    public String getGeozone_id() {
        return geozone_id;
    }

    public void setGeozone_id(String geozone_id) {
        this.geozone_id = geozone_id;
    }

    public String getDuration_type() {
        return duration_type;
    }

    public void setDuration_type(String duration_type) {
        this.duration_type = duration_type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AuthorizationRequestDTO{" +
                "drone_id='" + drone_id + '\'' +
                ", geozone_id='" + geozone_id + '\'' +
                ", duration_type='" + duration_type + '\'' +
                ", duration=" + duration +
                '}';
    }
}

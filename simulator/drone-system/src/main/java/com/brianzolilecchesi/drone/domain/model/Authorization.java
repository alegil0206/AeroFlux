package com.brianzolilecchesi.drone.domain.model;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;

public class Authorization {
    private String status;
    private Long id;
    private String drone_id;
    private String geozone_id;
    private String duration_type;
    private Integer duration;
    private String start_time;
    private String reason;
    private String end_time;
    private String revocation_time;

    public Authorization() {}

    public Authorization(Long id, String drone_id,
                         String geozone_id, String duration_type, Integer duration, String start_time, 
                         String status, String reason, String end_time, String revocation_time) {
        this.id = id;
        this.drone_id = drone_id;
        this.geozone_id = geozone_id;
        this.duration_type = duration_type;
        this.duration = duration;
        this.start_time = start_time;
        this.status = status;
        this.reason = reason;
        this.end_time = end_time;
        this.revocation_time = revocation_time;
    }

    public Authorization(AuthorizationResponseDTO dto) {
        this.id = dto.getId();
        this.drone_id = dto.getDrone_id();
        this.geozone_id = dto.getGeozone_id();
        this.duration_type = dto.getDuration_type();
        this.duration = dto.getDuration();
        this.start_time = dto.getStart_time();
        this.status = dto.getStatus();
        this.reason = dto.getReason();
        this.end_time = dto.getEnd_time();
        this.revocation_time = dto.getRevocation_time();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRevocation_time() {
        return revocation_time;
    }

    public void setRevocation_time(String revocation_time) {
        this.revocation_time = revocation_time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Authorization{")
          .append("id=").append(id)
          .append(", status=").append(status)
          .append(", drone_id='").append(drone_id).append('\'')
          .append(", geozone_id='").append(geozone_id).append('\'')
          .append(", duration_type='").append(duration_type).append('\'')
          .append(", duration=").append(duration)
          .append(", start_time='").append(start_time).append('\'');

        // Handle different statuses
        if ("GRANTED".equals(status)) {
            sb.append(", end_time='").append(end_time).append('\'');
        } else if ("DENIED".equals(status)) {
            sb.append(", reason='").append(reason).append('\'');
        } else if ("EXPIRED".equals(status)) {
            sb.append(", end_time='").append(end_time).append('\'');
        } else if ("REVOKED".equals(status)) {
            sb.append(", end_time='").append(end_time).append('\'')
              .append(", revocation_time='").append(revocation_time).append('\'');
        }

        sb.append('}');
        return sb.toString();
    }
}

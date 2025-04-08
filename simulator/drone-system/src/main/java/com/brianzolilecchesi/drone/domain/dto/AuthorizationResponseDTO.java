package com.brianzolilecchesi.drone.domain.dto;

public class AuthorizationResponseDTO {
    private String status;
    private Long id;
    private String drone_id;
    private String geozone_id;
    private String duration_type;
    private Integer duration;
    private String start_time;
    private String end_time;
    private String reason;
    private String revocation_time;

    public AuthorizationResponseDTO() {
    }

    public AuthorizationResponseDTO(String status, Long id, String drone_id, String geozone_id, String duration_type, 
                                    Integer duration, String start_time, String end_time, 
                                    String reason, String revocation_time) {
        this.status = status;
        this.id = id;
        this.drone_id = drone_id;
        this.geozone_id = geozone_id;
        this.duration_type = duration_type;
        this.duration = duration;
        this.start_time = start_time;
        this.end_time = end_time;
        this.reason = reason;
        this.revocation_time = revocation_time;
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
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return status;
    }
    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

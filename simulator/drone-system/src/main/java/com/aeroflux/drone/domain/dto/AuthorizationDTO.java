package com.aeroflux.drone.domain.dto;

import com.aeroflux.drone.domain.model.Authorization;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationDTO {
	
    private String status;
    private Long id;
    
    @JsonProperty("drone_id")
    private String droneId;
    
    @JsonProperty("geozone_id")
    private String geozoneId;
    
    @JsonProperty("duration_type")
    private String durationType;
    
    private Double duration;
    
    @JsonProperty("start_time")
    private String startTime;
    
    @JsonProperty("end_time")
    private String endTime;
    
    private String reason;
    
    @JsonProperty("revocation_time")
    private String revocationTime;

    public AuthorizationDTO(
    		String status, 
    		Long id, 
    		String droneId, 
    		String geozoneId, 
    		String durationType, 
    		Double duration, 
    		String startTime, 
    		String endTime, 
            String reason, 
            String revocationTime) {
    	
        this.status = status;
        this.id = id;
        this.droneId = droneId;
        this.geozoneId = geozoneId;
        this.durationType = durationType;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.revocationTime = revocationTime;
    }
    
    public AuthorizationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public String getGeozoneId() {
        return geozoneId;
    }

    public void setGeozoneId(String geozoneId) {
        this.geozoneId = geozoneId;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRevocationTime() {
        return revocationTime;
    }
    
    public void setRevocationTime(String revocationTime) {
        this.revocationTime = revocationTime;
    }
    
	public boolean isGranted() {
		return status.equalsIgnoreCase(Authorization.Status.GRANTED.getStatus());
	}

	@Override
    public String toString() {
		return String.format("AuthorizationDTO[id=%d, droneId=%s, geozoneId=%s, durationType=%s, duration=%d, startTime=%s, status=%s, reason=%s, endTime=%s, revocationTime=%s]",
				id, 
				droneId, 
				geozoneId, 
				durationType, 
				duration, 
				startTime, 
				status, 
				reason, 
				endTime, 
				revocationTime
				);
	}
    
    @Override
    public boolean equals(Object o) {
	            if (this == o) return true;
        if (!(o instanceof AuthorizationDTO)) return false;

        AuthorizationDTO that = (AuthorizationDTO) o;
        return id.equals(that.id);
    }
    
    @Override
	public int hashCode() {
		return id.hashCode();
	}
}

package com.brianzolilecchesi.drone.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequestDTO {
	
	@JsonProperty("drone_id")
    private String droneId;
	
	@JsonProperty("geozone_id")
    private String geozoneId;
    
	@JsonProperty("duration_type")
	private String durationType;
    private Double duration;

    public AuthorizationRequestDTO() {
    }

    public AuthorizationRequestDTO(String droneId, String geozoneId, String durationType, Double duration) {
        this.droneId = droneId;
        this.geozoneId = geozoneId;
        this.durationType = durationType;
        this.duration = duration;
    }
    
    public AuthorizationRequestDTO(String droneId, String geozoneId, String durationType) {
        this(droneId, geozoneId, durationType, null);
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

    @Override
    public String toString() {
		return String.format("AuthorizationRequestDTO [droneId=%s, geozoneId=%s, durationType=%s, duration=%s]", droneId, geozoneId, durationType, duration);
    }
    
    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AuthorizationRequestDTO))
			return false;

		AuthorizationRequestDTO that = (AuthorizationRequestDTO) o;

		if (!droneId.equals(that.droneId))
			return false;
		if (!geozoneId.equals(that.geozoneId))
			return false;
		if (!durationType.equals(that.durationType))
			return false;
		
		return duration.equals(that.duration);
	}
    
    @Override
    public int hashCode() {
        int result = droneId.hashCode();
        result = 31 * result + geozoneId.hashCode();
        result = 31 * result + durationType.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}

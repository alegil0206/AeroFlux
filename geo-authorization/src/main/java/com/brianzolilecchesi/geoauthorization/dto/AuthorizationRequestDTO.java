package com.brianzolilecchesi.geoauthorization.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequestDTO {
	
	private String droneId;
	private String geozoneId;
	private String duration; 
	
	@JsonCreator
	public AuthorizationRequestDTO(
			@JsonProperty("drone_id") String droneId, 
			@JsonProperty("geozone_id") String geozoneId,
			@JsonProperty("duration") String duration
			) {
		
		setDroneId(droneId);
		setGeozoneId(geozoneId);
		setDuration(duration);
	}
	
	public AuthorizationRequestDTO() {
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
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationRequestDTO[droneId=%s, geozoneId=%s, duration=%s]",
				getDroneId(),
				getGeozoneId(),
				getDuration()
				);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		AuthorizationRequestDTO authorizationRequestDTO = (AuthorizationRequestDTO) obj;
		return 
				getDroneId().equals(authorizationRequestDTO.getDroneId()) && 
				getGeozoneId().equals(authorizationRequestDTO.getGeozoneId()) &&
				getDuration().equals(authorizationRequestDTO.getDuration());
	}
}
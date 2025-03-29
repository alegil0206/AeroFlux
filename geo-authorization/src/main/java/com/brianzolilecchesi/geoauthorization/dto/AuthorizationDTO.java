package com.brianzolilecchesi.geoauthorization.dto;

import com.brianzolilecchesi.geoauthorization.util.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, 
		include = JsonTypeInfo.As.PROPERTY, 
		property = "status"
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = AuthorizationGrantedDTO.class, name = Constants.AUTHORIZATION_GRANTED_STATUS), 
	@JsonSubTypes.Type(value = AuthorizationDeniedDTO.class, name = Constants.AUTHORIZATION_DENIED_STATUS),
	@JsonSubTypes.Type(value = AuthorizationExpiredDTO.class, name = Constants.AUTHORIZATION_EXPIRED_STATUS),
	@JsonSubTypes.Type(value = AuthorizationRevokedDTO.class, name = Constants.AUTHORIZATION_REVOKED_STATUS)
	})
public abstract class AuthorizationDTO {
	
	private Long id;
	private String droneId;
	private String geozoneId;
	private String durationType;
	private int duration;
	private String startTime;
		
	@JsonCreator
	public AuthorizationDTO(
			@JsonProperty("id") Long id,
			@JsonProperty("drone_id") String droneId, 
			@JsonProperty("drone_operation_category") String droneOperationCategory,
			@JsonProperty("geozone_id") String geozoneId,
			@JsonProperty("geozone_category") String geozoneCategory,
			@JsonProperty("duration_type") String durationType,
			@JsonProperty("duration") int duration,
			@JsonProperty("start_time") String startTime
			) {
		
		setId(id);
		setDroneId(droneId);
		setGeozoneId(geozoneId);
		setDurationType(durationType);
		setDuration(duration);
		setStartTime(startTime);
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
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
		
	@Override
	public String toString() {
		return String.format(
				"AuthorizationDTO[id=%s, droneId=%s, geozoneId=%s, durationType=%s, duration=%s, startTime=%s]", 
				id, droneId, geozoneId, durationType, duration, startTime
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
		AuthorizationDTO authorizationDTO = (AuthorizationDTO) obj;
		return getId().equals(authorizationDTO.getId());
	}
}
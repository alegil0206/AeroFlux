package com.aeroflux.geoauthorization.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRevokedDTO extends AuthorizationDTO {
	
	private String endTime;
	private String revocationTime;
	
	@JsonCreator
	public AuthorizationRevokedDTO(
			@JsonProperty("id") Long id,
			@JsonProperty("drone_id") String droneId, 
			@JsonProperty("drone_operation_category") String droneOperationCategory,
			@JsonProperty("geozone_id") String geozoneId,
			@JsonProperty("geozone_category") String geozoneCategory,
			@JsonProperty("duration_type") String durationType,
			@JsonProperty("duration") int duration,
			@JsonProperty("start_time") String startTime,
			@JsonProperty("end_time") String endTime,
			@JsonProperty("revocation_time") String revocationTime
			) {
		
		super(id, droneId, droneOperationCategory, geozoneId, geozoneCategory, durationType, duration, startTime);
		setEndTime(endTime);
		setRevocationTime(revocationTime);
		
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getRevocationTime() {
		return revocationTime;
	}
	
	public void setRevocationTime(String revocationTime) {
		this.revocationTime = revocationTime;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationGrantedDTO[%s, endTime=%s, revocationTime=%s]",
				super.toString(), getEndTime(), getRevocationTime()
				);		
	}

}
package com.brianzolilecchesi.geoauthorization.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationGrantedDTO extends AuthorizationDTO {
	
	private String endTime;
	
	@JsonCreator
	public AuthorizationGrantedDTO(
			@JsonProperty("id") Long id,
			@JsonProperty("drone_id") String droneId, 
			@JsonProperty("drone_operation_category") String droneOperationCategory,
			@JsonProperty("geozone_id") String geozoneId,
			@JsonProperty("geozone_category") String geozoneCategory,
			@JsonProperty("duration_type") String durationType,
			@JsonProperty("duration") int duration,
			@JsonProperty("start_time") String startTime,
			@JsonProperty("end_time") String endTime
			) {
		
		super(id, droneId, droneOperationCategory, geozoneId, geozoneCategory, durationType, duration, startTime);
		setEndTime(endTime);
		
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationGrantedDTO[%s, endTime=%s]",
				super.toString(), getEndTime()
				);		
	}

}

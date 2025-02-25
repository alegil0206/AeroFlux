package com.brianzolilecchesi.geoauthorization.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationDeniedDTO extends AuthorizationDTO {
	
	private String reason;
	
	@JsonCreator
	public AuthorizationDeniedDTO(
			@JsonProperty("id") Long id,
			@JsonProperty("drone_id") String droneId, 
			@JsonProperty("drone_operation_category") String droneOperationCategory,
			@JsonProperty("geozone_id") String geozoneId,
			@JsonProperty("geozone_category") String geozoneCategory,
			@JsonProperty("duration_type") String durationType,
			@JsonProperty("duration") int duration,
			@JsonProperty("start_time") String startTime,
			@JsonProperty("reason") String reason
			) {
		
		super(id, droneId, droneOperationCategory, geozoneId, geozoneCategory, durationType, duration, startTime);
		setReason(reason);
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationDeniedDTO[%s, reason=%s]",
				super.toString(), getReason()
				);
	}
}
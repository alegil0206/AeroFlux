package com.aeroflux.geoawareness.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CircularGeozoneDTO extends GeozoneDTO {
	
	private double latitude;
	private double longitude;
	private double radius;

	@JsonCreator
	public CircularGeozoneDTO(
			@JsonProperty("id") String id, 
			@JsonProperty("name") String name, 
			@JsonProperty("category") String category,
			@JsonProperty("status") String status,
			@JsonProperty("altitude_level_limit_inferior") String altitudeLevelLimitInferior,
			@JsonProperty("altitude_limit_inferior") Double altitudeLimitInferior,
			@JsonProperty("altitude_level_limit_superior") String altitudeLevelLimitSuperior,
			@JsonProperty("altitude_limit_superior") Double altitudeLimitSuperior,
			@JsonProperty("longitude") double longitude, 
			@JsonProperty("latitude") double latitude, 
			@JsonProperty("radius") double radius 
			) {
		super(id, name, category, status, altitudeLevelLimitInferior, altitudeLimitInferior, altitudeLevelLimitSuperior, altitudeLimitSuperior);
		setLongitude(longitude);
		setLatitude(latitude);
		setRadius(radius);
	}
		
	public CircularGeozoneDTO() {
		super();
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	@Override
	public String toString() {
		return String.format(
				"CircularGeozoneDTO[%s, longitude=%f, latitude=%f, radius=%f]",
				super.toString(),
				getLongitude(), 
				getLatitude(), 
				getRadius() 
				);
	}
}
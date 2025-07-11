package com.aeroflux.geoawareness.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PolygonalGeozoneDTO extends GeozoneDTO {

	private List<double[]> coordinates;
	
	@JsonCreator
	public PolygonalGeozoneDTO(
			@JsonProperty("id") String id, 
			@JsonProperty("name") String name, 
			@JsonProperty("category") String category,
			@JsonProperty("status") String status,
			@JsonProperty("altitude_level_limit_inferior") String altitudeLevelLimitInferior,
			@JsonProperty("altitude_limit_inferior") Double altitudeLimitInferior,
			@JsonProperty("altitude_level_limit_superior") String altitudeLevelLimitSuperior,
			@JsonProperty("altitude_limit_superior") Double altitudeLimitSuperior,
			@JsonProperty("coordinates") List<double[]> coordinates
			) {
		super(id, name, category, status, altitudeLevelLimitInferior, altitudeLimitInferior, altitudeLevelLimitSuperior, altitudeLimitSuperior);
		setCoordinates(coordinates);
	}
	
	public PolygonalGeozoneDTO() {
		super();
	}
	
	public List<double[]> getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(List<double[]> coordinates) {
		this.coordinates = coordinates;
	}
	
	@Override
	public String toString() {
		return String.format(
				"PolygonalGeozoneDTO[%s, coordinates=%s]",
				super.toString(),
				coordinates
				);
	}
}
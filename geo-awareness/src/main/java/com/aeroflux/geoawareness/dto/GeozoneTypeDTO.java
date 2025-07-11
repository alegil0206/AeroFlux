package com.aeroflux.geoawareness.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeozoneTypeDTO {
	
	private final String name;
	private final String description;
	private final GeozoneDTO exampleGeozoneDTO;

	@JsonCreator
	public GeozoneTypeDTO(
			String name, 
			String description, 
			@JsonProperty("example") GeozoneDTO exampleGeozoneDTO
			) {
		
		this.name = name;
        this.description = description;
        this.exampleGeozoneDTO = exampleGeozoneDTO;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public GeozoneDTO getExampleGeozoneDTO() {
		return exampleGeozoneDTO;
	}
	
	@Override
	public String toString() {
		return String.format(
				"GeozoneTypeDTO[name=%s, description=%s, exampleGeozoneDTO=%s]", 
				name, 
				description,
				exampleGeozoneDTO
				);
	}
}
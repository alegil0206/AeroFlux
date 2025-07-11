package com.aeroflux.geoawareness.dto;

import com.aeroflux.geoawareness.util.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, 
		include = JsonTypeInfo.As.PROPERTY, 
		property = "type"
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = CircularGeozoneDTO.class, name = Constants.CIRCULAR_GEOZONE_TYPE), 
	@JsonSubTypes.Type(value = PolygonalGeozoneDTO.class, name = Constants.POLYGONAL_GEOZONE_TYPE)
	})
public abstract class GeozoneDTO {
	
	private String id;
    private String name;
    private String category;
    private String status;
    private String altitudeLevelLimitInferior;
    private Double altitudeLimitInferior;
	private String altitudeLevelLimitSuperior;
	private Double altitudeLimitSuperior;

    @JsonCreator
    public GeozoneDTO(
            @JsonProperty("id") String id, 
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("status") String status,
            @JsonProperty("altitude_level_limit_inferior") String altitudeLevelLimitInferior,
            @JsonProperty("altitude_limit_inferior") Double altitudeLimitInferior,
			@JsonProperty("altitude_level_limit_superior") String altitudeLevelLimitSuperior,
			@JsonProperty("altitude_limit_superior") Double altitudeLimitSuperior
            ) {
    	
        setId(id);
        setName(name);
        setCategory(category);
		setStatus(status);
		setAltitudeLevelLimitInferior(altitudeLevelLimitInferior);
		setAltitudeLimitInferior(altitudeLimitInferior);
		setAltitudeLevelLimitSuperior(altitudeLevelLimitSuperior);
		setAltitudeLimitSuperior(altitudeLimitSuperior);
	}

	public GeozoneDTO() {
	}
        
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    public String getCategory() {
    	return category;
    }
    
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAltitudeLevelLimitInferior() {
		return altitudeLevelLimitInferior;
	}
	
	public void setAltitudeLevelLimitInferior(String altitudeLevel) {
		this.altitudeLevelLimitInferior = altitudeLevel;
	}
	
	public Double getAltitudeLimitInferior() {
		return altitudeLimitInferior;
	}
	
	public void setAltitudeLimitInferior(Double altitude) {
		this.altitudeLimitInferior = altitude;
	}

	public String getAltitudeLevelLimitSuperior() {
		return altitudeLevelLimitSuperior;
	}

	public void setAltitudeLevelLimitSuperior(String altitudeLevel) {
		this.altitudeLevelLimitSuperior = altitudeLevel;
	}

	public Double getAltitudeLimitSuperior() {
		return altitudeLimitSuperior;
	}

	public void setAltitudeLimitSuperior(Double altitude) {
		this.altitudeLimitSuperior = altitude;
	}
		
    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		GeozoneDTO geozone = (GeozoneDTO) obj;
		return id.equals(geozone.id);
	}

    @Override
	public String toString() {
    	return String.format(
				"GeozoneDTO[id=%s, name=%s, category=%s, status=%s, altitudeLevelLimitInferior=%s, altitudeLimitInferior=%f, altitudeLevelLimitSuperior=%s, altitudeLimitSuperior=%f]",
				id, 
				name,
				category,
				status,
				altitudeLevelLimitInferior,
				altitudeLimitInferior,
				altitudeLevelLimitSuperior,
				altitudeLimitSuperior
				);
	}
}
package com.brianzolilecchesi.geoawareness.dto;

import com.brianzolilecchesi.geoawareness.util.Constants;
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
    private String altitudeLevel;
    private Double altitude;

    @JsonCreator
    public GeozoneDTO(
            @JsonProperty("id") String id, 
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("status") String status,
            @JsonProperty("altitude_level") String altitudeLevel,
            @JsonProperty("altitude") Double altitude
            ) {
    	
        setId(id);
        setName(name);
        setCategory(category);
		setStatus(status);
		setAltitudeLevel(altitudeLevel);
		setAltitude(altitude);
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
	
	public String getAltitudeLevel() {
		return altitudeLevel;
	}
	
	public void setAltitudeLevel(String altitudeLevel) {
		this.altitudeLevel = altitudeLevel;
	}
	
	public Double getAltitude() {
		return altitude;
	}
	
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
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
				"GeozoneDTO[id=%s, name=%s, category=%s, status=%s, altitudeLevel=%s, altitude=%f]",
				id, 
				name,
				category,
				status,
				altitudeLevel,
				altitude
				);
	}
}
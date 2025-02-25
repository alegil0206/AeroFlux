package com.brianzolilecchesi.geoawareness.model.persistency.geozone;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.model.persistency.Altitude;

public abstract class Geozone {

    @Id
    private String id;

    @Field("name")
    private String name;
    
    @Field("category")
    private String category;
    
    @Field("status")
    private String status;
    
    @Field("altitude")
    private Altitude altitude;
    
    @Field("type")
    private String type;
  
    Geozone(String name, String type, String category, String status, final Altitude altitude) {
        setName(name);
        setType(type);
        setCategory(category);
        setStatus(status);
        setAltitude(altitude);
    }
        
	Geozone() {
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
    
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String authorization) {
		this.category = authorization;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Altitude getAltitude() {
		return altitude;
	}
	
	public void setAltitude(final Altitude height) {
		this.altitude = height;
	}
	
	protected void update(final GeozoneDTO geozoneDTO) {
		assert geozoneDTO != null;
		
		setName(geozoneDTO.getName());
		setCategory(geozoneDTO.getCategory());
		setStatus(geozoneDTO.getStatus());
		getAltitude().setName(geozoneDTO.getAltitudeLevel());
		getAltitude().setValue(geozoneDTO.getAltitude().intValue());
	}
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Geozone geozone = (Geozone) obj;
		return Objects.equals(name, geozone.name);
	}

    @Override
	public String toString() {
    	return String.format(
				"Geozone[id=%s, name=%s, status=%s, category=%s, altitude=%s, type=%s]",
				id, 
				name,
				status,
				category,
				altitude.toString(),
				type
				);
	}
}
package com.brianzolilecchesi.geoauthorization.model.persistency.geozone;

import com.brianzolilecchesi.geoauthorization.dto.PublishedGeozoneDTO;
import com.brianzolilecchesi.geoauthorization.util.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Geozone {
	
	@Id
	private String id;
	@NotNull
	private String category;
	@NotNull
	private String status;
	
	Geozone(String id, String category, String status) {
		setId(id);
		setCategory(category);
		setStatus(status);
	}
			
	Geozone() {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public boolean isActive() {
		return status.equals(Constants.GEOZONE_ACTIVE_STATUS);
	}
	
	public void update(final PublishedGeozoneDTO geozoneDTO) {
        setCategory(geozoneDTO.getCategory());
        setStatus(geozoneDTO.getStatus());
	}
	
	@Override
	public String toString() {
		return String.format("Geozone[id=%s, category=%s, status=%s]",
				id, category, status
				);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Geozone)) {
			return false;
		}
		Geozone geozone = (Geozone) obj;
		return id.equals(geozone.id);
	}
}
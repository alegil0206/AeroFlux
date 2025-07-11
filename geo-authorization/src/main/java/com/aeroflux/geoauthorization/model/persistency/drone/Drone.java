package com.aeroflux.geoauthorization.model.persistency.drone;

import com.aeroflux.geoauthorization.dto.PublishedDroneDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Drone {
	
	@Id
	private String id;
	
	@NotNull
	private String operationCategory;
		
	Drone(String id, String operationCategory) {
		setId(id);
		setOperationCategory(operationCategory);
	}
			
	Drone() {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	
	public String getOperationCategory() {
		return operationCategory;
	}
	
	public void update(final PublishedDroneDTO droneDTO) {
        setOperationCategory(droneDTO.getOperationCategory());
	}
	
	@Override
	public String toString() {
		return String.format("Drone[id=%s, operationCategory=%s]", id, operationCategory);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Drone)) {
			return false;
		}
		Drone geozone = (Drone) obj;
		return id.equals(geozone.id);
	}
}

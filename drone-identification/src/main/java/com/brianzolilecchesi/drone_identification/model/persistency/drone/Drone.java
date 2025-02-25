package com.brianzolilecchesi.drone_identification.model.persistency.drone;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.brianzolilecchesi.drone_identification.dto.PositionDTO;
import com.brianzolilecchesi.drone_identification.util.Constants;

@Document(collection = Constants.DRONE_COLLECTION_NAME)
public class Drone {
	
	@Id
	private String id;
	
	@Field("name")
	private String name;
	@Field("model")
	private String model;
	@Field("owner")
	private String owner;
	@Field("operation_category")
	private String operationCategory;
	
	@Field("plan_definition_timestamp")
	private LocalDateTime planDefinitionTimestamp;
	
	@Field("source")
	private GeoJsonPoint source;
	@Field("destination")
	private GeoJsonPoint destination;

	Drone(
			final String name, 
			final String model, 
			final String owner, 
			final String operationCategory, 
			final LocalDateTime registrationTimestamp,
			final GeoJsonPoint source,
			final GeoJsonPoint destination
			) {
		
		setName(name);
		setModel(model);
		setOwner(owner);
		setOperationCategory(operationCategory);
		setPlanDefinitionTimestamp(registrationTimestamp);
		setSource(source);
		setDestination(destination);
	}
	
	Drone() {
	}
	
	public String getId() {
		return id;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	public String getModel() {
		return model;
	}
	
	void setModel(String model) {
		this.model = model;
	}
	
	public String getOwner() {
		return owner;
	}
	
	void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOperationCategory() {
		return operationCategory;
	}
	
	void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	
	public LocalDateTime getPlanDefinitionTimestamp() {
		return planDefinitionTimestamp;
	}
	
	void setPlanDefinitionTimestamp(LocalDateTime planDefinitionTimestamp) {
        this.planDefinitionTimestamp = planDefinitionTimestamp;
    }
	
	public GeoJsonPoint getSource() {
		return source;
	}
	
	void setSource(GeoJsonPoint source) {
		this.source = source;
	}
	
	void setSource(PositionDTO source) {
		this.source = new GeoJsonPoint(source.getLongitude(), source.getLatitude());
	}
	
	public GeoJsonPoint getDestination() {
		return destination;
	}
	
	void setDestination(GeoJsonPoint destination) {
        this.destination = destination;
	}
	
	void setDestination(PositionDTO destination) {
		this.destination = new GeoJsonPoint(destination.getLongitude(), destination.getLatitude());
	}
	
	private boolean isSamePosition(final PositionDTO positionDTO, final GeoJsonPoint point) {
		return 
				positionDTO.getLongitude() == point.getX() && 
				positionDTO.getLatitude() == point.getY();
	}
	
	public boolean isSameSource(final PositionDTO positionDTO) {
		return isSamePosition(positionDTO, source);
	}
	
	public boolean isSameDestination(final PositionDTO positionDTO) {
		return isSamePosition(positionDTO, destination);
	}
		
	@Override
	public String toString() {
		return String.format(
				"Drone[id=%s, name=%s, model=%s, owner=%s, operationCategory=%s, planDefinitionTimestamp=%s, source=%s, destination=%s]",
				id,
				name,
				model,
				owner,
				operationCategory,
				planDefinitionTimestamp,
				source,
				destination
				);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Drone drone = (Drone) o;
		return id.equals(drone.id);
	}
}
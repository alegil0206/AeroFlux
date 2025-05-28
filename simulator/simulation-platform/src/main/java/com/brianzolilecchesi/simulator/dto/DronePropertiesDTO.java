package com.brianzolilecchesi.simulator.dto;

import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DronePropertiesDTO {
	
	private String id;
	private String name;
	private String model;
	private String owner;
	private String operationCategory;
	private String planDefinitionTimestamp;
	private AdaptiveCapabilitiesDTO adaptiveCapabilities;
	private double battery;
	private PositionDTO source;
	private PositionDTO destination;

	public DronePropertiesDTO() {
	}

	@JsonCreator
	public DronePropertiesDTO(
			@JsonProperty("id") String id, 
			@JsonProperty("name") String name, 
			@JsonProperty("model") String model, 
			@JsonProperty("owner") String owner,
			@JsonProperty("operation_category") String operationCategory,
			@JsonProperty("plan_definition_timestamp") String planDefinitionTimestamp,
			@JsonProperty("adaptive_capabilities") AdaptiveCapabilitiesDTO adaptiveCapabilities,
			@JsonProperty("battery") double battery,
			@JsonProperty("source") PositionDTO source,
			@JsonProperty("destination") PositionDTO destination
			) {
		
		setId(id);
		setName(name);
		setModel(model);
		setOwner(owner);
		setOperationCategory(operationCategory);
		setPlanDefinitionTimestamp(planDefinitionTimestamp);
		setAdaptiveCapabilities(adaptiveCapabilities);
		setBattery(battery);
		setSource(source);
		setDestination(destination);
	}

	public DronePropertiesDTO(DroneProperties droneProperties) {
		this.id = droneProperties.getId();
		this.name = droneProperties.getName();
		this.model = droneProperties.getModel();
		this.owner = droneProperties.getOwner();
		this.operationCategory = droneProperties.getOperationCategory();
		this.planDefinitionTimestamp = droneProperties.getPlanDefinitionTimestamp();
		this.adaptiveCapabilities = new AdaptiveCapabilitiesDTO(droneProperties.getAdaptiveCapabilities());
		this.battery = droneProperties.getBattery();
		this.source = new PositionDTO(droneProperties.getSource().getLongitude(), droneProperties.getSource().getLatitude());
		this.destination = new PositionDTO(droneProperties.getDestination().getLongitude(), droneProperties.getDestination().getLatitude());
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOperationCategory() {
		return operationCategory;
	}
	
	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	
	public String getPlanDefinitionTimestamp() {
		return planDefinitionTimestamp;
	}
	
	public void setPlanDefinitionTimestamp(String planDefinitionTimestamp) {
		this.planDefinitionTimestamp = planDefinitionTimestamp;
	}
	
	public PositionDTO getSource() {
		return source;
	}
	
	public void setSource(PositionDTO source) {
		this.source = source;
	}
	
	public PositionDTO getDestination() {
		return destination;
	}
	
	public void setDestination(PositionDTO destination) {
		this.destination = destination;
	}

	public AdaptiveCapabilitiesDTO getAdaptiveCapabilities() {
		return adaptiveCapabilities;
	}

	public void setAdaptiveCapabilities(AdaptiveCapabilitiesDTO adaptiveCapabilities) {
		this.adaptiveCapabilities = adaptiveCapabilities;
	}

	public double getBattery() {
		return battery;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}
	
	@Override
	public String toString() {
		return String.format(
                "Drone[id=%s, name=%s, model=%s, owner=%s, operationCategory=%s, planDefinitionTimestamp=%s, adaptiveCapabilities=%s, battery=%s, source=%s, destination=%s]",
                id,
                name,
                model,
                owner,
                operationCategory,
                planDefinitionTimestamp,
				adaptiveCapabilities,
				battery,
                source,
                destination
                );
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		DronePropertiesDTO other = (DronePropertiesDTO) obj;
		return id != null && id.equals(other.id);
	}
}
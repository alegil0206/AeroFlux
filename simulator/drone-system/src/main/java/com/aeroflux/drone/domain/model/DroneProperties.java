package com.aeroflux.drone.domain.model;

public class DroneProperties {
	
	private String id;
	private String name;
	private String model;
	private String owner;
	private String operationCategory;
	private String planDefinitionTimestamp;
	private AdaptiveCapabilities adaptiveCapabilities;
	private double battery;
	private Position source;
	private Position destination;

	public static final String DRONE_SPECIFIC_OPERATION_CATEGORY = "SPECIFIC";
	public static final String DRONE_CERTIFIED_OPERATION_CATEGORY = "CERTIFIED";

	public DroneProperties() {
	}

	public DroneProperties( 
			String id, 
			String name, 
			String model, 
			String owner,
			String operationCategory,
			String planDefinitionTimestamp,
			AdaptiveCapabilities adaptiveCapabilities,
			double battery,
			Position source,
			Position destination
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
	
	public Position getSource() {
		return source;
	}
	
	public void setSource(Position source) {
		this.source = source;
	}
	
	public Position getDestination() {
		return destination;
	}
	
	public void setDestination(Position destination) {
		this.destination = destination;
	}

	public AdaptiveCapabilities getAdaptiveCapabilities() {
		return adaptiveCapabilities;
	}

	public void setAdaptiveCapabilities(AdaptiveCapabilities adaptiveCapabilities) {
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
		DroneProperties other = (DroneProperties) obj;
		return id != null && id.equals(other.id);
	}
}
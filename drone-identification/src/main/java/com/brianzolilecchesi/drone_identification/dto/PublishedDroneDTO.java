package com.brianzolilecchesi.drone_identification.dto;

public class PublishedDroneDTO {
	
	private String id;
	private String operationCategory;
		
	public PublishedDroneDTO(String id, String operationCategory) {
		setId(id);
		setOperationCategory(operationCategory);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOperationCategory() {
		return operationCategory;
	}
	
	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}
	
	@Override
	public String toString() {
		return String.format("PublishedDroneDTO[id=%s, operationCategory=%s]", id, operationCategory);

	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PublishedDroneDTO other = (PublishedDroneDTO) obj;
		return id.equals(other.id);
	}
}
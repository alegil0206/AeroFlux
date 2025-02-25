package com.brianzolilecchesi.drone_identification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationCategoryDTO {
	
	private String name;
	private String description;

	@JsonCreator
	public OperationCategoryDTO(
			@JsonProperty("name") String name, 
			@JsonProperty("description") String description
			) {
		setName(name);
		setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return String.format("OperationCategoryDTO[name=%s, description=%s]", name, description);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		OperationCategoryDTO other = (OperationCategoryDTO) obj;
		return this.name.equalsIgnoreCase(other.name);
	}
}
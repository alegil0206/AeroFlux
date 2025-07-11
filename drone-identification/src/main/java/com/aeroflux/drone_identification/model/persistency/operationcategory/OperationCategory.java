package com.aeroflux.drone_identification.model.persistency.operationcategory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.aeroflux.drone_identification.util.Constants;

@Document(collection = Constants.OPERATION_CATEGORY_COLLECTION_NAME)
public class OperationCategory {
	
	@Id
	private String id;
	
	@Field("name")
	private String name;
	
	@Field("description")
	private String description;
	
	public OperationCategory(String name, String description) {
		setName(name);
		setDescription(description);
	}
	
	public OperationCategory() {
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
		return String.format("OperationCategory[name=%s, description=%s]", name, description);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		OperationCategory other = (OperationCategory) obj;
		return this.name.equalsIgnoreCase(other.name);
	}
}
package com.aeroflux.geoawareness.model.persistency;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.aeroflux.geoawareness.util.Constants;

@Document(collection = Constants.CATEGORY_COLLECTION_NAME)
public class Category {
	
	@Field("name")
	private String name;
	
	@Field("description")
	private String description;
	
	public Category(String name, String description) {
		setName(name);
		setDescription(description);
	}
	
	Category() {
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
		return String.format("Category[name=%s, description=%s]", name, description);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Category other = (Category) obj;
        return Objects.equals(name, other.name);
	}
}
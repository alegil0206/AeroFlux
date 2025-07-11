package com.aeroflux.geoawareness.dto;

public class PublishedGeozoneDTO {
	
	private String id;
	private String category;
	private String status;
		
	public PublishedGeozoneDTO(String id, String category, String status) {
		setId(id);
		setCategory(category);
		setStatus(status);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return String.format(
				"PusblishedGeozoneDTO[id=%s, category=%s, status=%s]",
				id, category, status);
	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PublishedGeozoneDTO other = (PublishedGeozoneDTO) obj;
		return id.equals(other.id);
	}
}

package com.aeroflux.geoauthorization.dto;

public class DurationDTO {
	
	private String name;
	private int value;
	private String unit;
	
	public DurationDTO(String name, int i, String unit) {
		setName(name);
		setValue(i);
		setUnit(unit);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public String toString() {
		return String.format("DurationDTO[name=%s, value=%s, unit=%s]", name, value, unit);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		DurationDTO other = (DurationDTO) obj;
		return name.equals(other.name);
	}
}
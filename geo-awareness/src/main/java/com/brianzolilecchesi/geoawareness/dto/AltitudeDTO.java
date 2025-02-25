package com.brianzolilecchesi.geoawareness.dto;

public class AltitudeDTO {
	
	private String level;
	private int altitude;
	private String unit;
	
	public AltitudeDTO(String level, int altitude, String unit) {
		setLevel(level);
		setAltitude(altitude);
		setUnit(unit);
	}
	
	public AltitudeDTO() {
	}
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public int getAltitude() {
		return altitude;
	}
	
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public String toString() {
		return String.format("AltitudeDTO[level=%s, altitude=%s, unit=%s]", level, altitude, unit);
	}
}
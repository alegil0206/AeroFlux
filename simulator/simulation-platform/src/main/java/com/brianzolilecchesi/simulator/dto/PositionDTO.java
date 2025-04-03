package com.brianzolilecchesi.simulator.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.brianzolilecchesi.drone.domain.model.Position;

public class PositionDTO {
	
	private double longitude;
    private double latitude;
    private double altitude;

    @JsonCreator
    public PositionDTO(
    		@JsonProperty("longitude") double longitude,
    		@JsonProperty("latitude") double latitude,
            @JsonProperty("altitude") double altitude
            ) {
        
    	setLongitude(longitude);
		setLatitude(latitude);
		setAltitude(altitude);
    }

	public PositionDTO(Position position) {
		this(position.getLongitude(), position.getLatitude(), position.getAltitude());
	}
    
    public PositionDTO(double longitude, double latitude) {
		this(longitude, latitude, 0.0);
    }

    public double getLatitude() {
        return latitude;
    }
    
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }
    
	public void setAltitude(double height) {
		this.altitude = height;
	}
	
	public String toString() {
		return String.format(
				"PositionDTO[longitude=%f, latitude=%f, altitude=%f]",
				latitude, 
				longitude, 
				altitude
				);
	}
	
	public boolean equals(PositionDTO obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PositionDTO other = (PositionDTO) obj;
		return 
                latitude == other.getLatitude() &&
                longitude == other.getLongitude() &&
                altitude == other.getAltitude();
	}
}
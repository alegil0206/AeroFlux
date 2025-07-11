package com.aeroflux.drone.domain.model;

import com.aeroflux.drone.domain.geo.GeoCalculatorFactory;

public class Coordinate {
	
	protected final double latitude;
	protected final double longitude;
	
	public Coordinate(double latitude, double longitude) {
		assert latitude >= -90 && latitude <= 90;
		assert longitude >= -180 && longitude <= 180;
		
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double distance(Coordinate other) {
		return GeoCalculatorFactory.getGeoDistanceCalculator().distance(this, other);
	}
	
	public Coordinate move(double latitudeDisplacement, double longitudeDisplacement) {
		return GeoCalculatorFactory
				.getGeoDistanceCalculator()
				.moveByDisplacement(this, latitudeDisplacement, longitudeDisplacement);
	}
	
	@Override
	public String toString() {
		return String.format(
				"Coordinate[latitude=%s, longitude=%s]", 
				latitude, 
				longitude
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

		Coordinate other = (Coordinate) obj;
		return Double.compare(other.latitude, latitude) == 0 && 
			   Double.compare(other.longitude, longitude) == 0;
	}

	@Override
	public int hashCode() {
		int result = Double.hashCode(latitude);
		result = 31 * result + Double.hashCode(longitude);
		return result;
	}
}

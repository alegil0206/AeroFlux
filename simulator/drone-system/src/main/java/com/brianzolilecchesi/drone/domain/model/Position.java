package com.brianzolilecchesi.drone.domain.model;

public class Position extends Coordinates {
    private double altitude;

	public Position(double latitude, double longitude, double altitude) {
		super(latitude, longitude);
		this.altitude = altitude;
	}

	public Position(Coordinates coordinates, double altitude) {
		super(coordinates.getLatitude(), coordinates.getLongitude());
		this.altitude = altitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return "Position [latitude=" + getLatitude() + ", longitude=" + getLongitude() + ", altitude=" + altitude + "]";
	}

	private static final double THRESHOLD = 1e-6;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Position))
			return false;
		Position other = (Position) obj;
		
		return Math.abs(this.getLatitude() - other.getLatitude()) < THRESHOLD &&
			   Math.abs(this.getLongitude() - other.getLongitude()) < THRESHOLD &&
			   Math.abs(this.altitude - other.altitude) < THRESHOLD;
	}
}
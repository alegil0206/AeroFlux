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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (Double.doubleToLongBits(altitude) != Double.doubleToLongBits(other.altitude))
			return false;
		return true;
	}

}
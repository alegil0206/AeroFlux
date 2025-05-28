package com.brianzolilecchesi.drone.domain.model.shape;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public class GeoCircle extends GeoShape {
	
	private final Coordinate center;
	private final double radius;
	
	public GeoCircle(final Coordinate center, double radius) {
		assert center != null;
		assert radius > 0;
		
		this.center = center;
		this.radius = radius;
	}
	
	public Coordinate getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	@Override
	public String toString() {
		return String.format("Circle[center=%s, radius=%s]", center, radius);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		GeoCircle other = (GeoCircle) obj;
		return Double.compare(other.radius, radius) == 0 && center.equals(other.center);
	}
	
	@Override
	public int hashCode() {
		int result = center.hashCode();
		long temp = Double.doubleToLongBits(radius);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
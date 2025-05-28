package com.brianzolilecchesi.drone.domain.model.shape;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public class GeoPolygon extends GeoShape {
	
	protected final List<Coordinate> points;
	
	public GeoPolygon(final List<Coordinate> points) {
		assert points != null;
		assert points.size() >= 3;

		this.points = points;
	}
	
	public List<Coordinate> getPoints() {
		return new ArrayList<>(points);
	}
	
	@Override
	public String toString() {
		return String.format("GeoPolygon[points=%s]", points);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		GeoPolygon other = (GeoPolygon) obj;
		return points.equals(other.points);
	}
	
	@Override
	public int hashCode() {
		return points.hashCode();
	}
}
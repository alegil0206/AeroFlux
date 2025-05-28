package com.brianzolilecchesi.drone.domain.model.shape;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public class GeoRectangle extends GeoPolygon {
		
	public GeoRectangle(final Coordinate nw, final Coordinate ne, final Coordinate sw, final Coordinate se) {
		super(List.of(nw, ne, sw, se));
		
		assert nw != null;
		assert ne != null;
		assert sw != null;
		assert se != null;
	}
	
	public GeoRectangle(final Coordinate ne, final Coordinate sw) {
		this(
				new Coordinate(ne.getLatitude(), sw.getLongitude()),
				ne,
				sw,
				new Coordinate(sw.getLatitude(), ne.getLongitude())
			);
	}
	
	public Coordinate getNw() {
		return points.get(0);
	}
	
	public Coordinate getNe() {
		return points.get(1);
	}
	
	public Coordinate getSw() {
		return points.get(2);
	}
	
	public Coordinate getSe() {
		return points.get(3);
	}
	
	@Override
	public String toString() {
		return String.format("GeoRectangle[%s]", super.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		GeoRectangle other = (GeoRectangle) obj;
		assert other.points.size() == 4;
		return points.get(0).equals(other.points.get(0)) && 
			   points.get(1).equals(other.points.get(1)) && 
			   points.get(2).equals(other.points.get(2)) && 
			   points.get(3).equals(other.points.get(3));
		
	}
	
	@Override
	public int hashCode() {
		return points.hashCode();
	}
}
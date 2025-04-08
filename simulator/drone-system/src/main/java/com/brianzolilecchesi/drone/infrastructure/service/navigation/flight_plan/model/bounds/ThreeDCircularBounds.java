package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;

public class ThreeDCircularBounds extends ThreeDBounds {
	
	private Position center;
	private double radius;
	private double height;
	
	public ThreeDCircularBounds(final Position center, final double radius, final double height) {
		assert radius >= 0;
		assert height >= 0;
		
		setCenter(center);
		setRadius(radius);
		setHeight(height);
	}
	
	public Position getCenter() {
		return center;
	}
	
	private void setCenter(final Position center) {
		this.center = center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	private void setRadius(final double radius) {
		this.radius = radius;
	}
	
	public double getHeight() {
		return height;
	}
	
	private void setHeight(final double height) {
		this.height = height;
	}
	
	@Override
	public double getStartingAltitude() {
		return center.getAltitude() - height / 2;
	}
	
	@Override
	public double getEndingAltitude() {
		return center.getAltitude() + height / 2;
	}
	
	@Override
	public ThreeDBoundingBox getBoundingBox() {
		return new ThreeDBoundingBox(
				center.move(radius, -radius, height / 2),		// Top North West
				center.move(radius, -radius, -height / 2),		// Bottom North West
				
				center.move(radius, radius, height / 2),		// Top North East
				center.move(radius, radius, -height / 2),		// Bottom North East
				
				center.move(-radius, radius, height / 2),		// Top South East
				center.move(-radius, radius, -height / 2),		// Bottom South East
				
				center.move(-radius, -radius, height / 2),		// Top South West
				center.move(-radius, -radius, -height / 2)		// Bottom South West
				);
	}
	
	@Override
	public boolean contains(final Position coordinate) {
		assert coordinate != null;
		
		if (
				coordinate.getAltitude() < getStartingAltitude() ||
				coordinate.getAltitude() > getEndingAltitude()
			) {
			return false;
		}
		
		if (center.distance(coordinate, false) > radius) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean contains(final ThreeDBounds bounds) {
		if (bounds instanceof ThreeDCircularBounds) {
			ThreeDCircularBounds other = (ThreeDCircularBounds) bounds;
			if (
					other.getStartingAltitude() < getStartingAltitude() || 
					other.getEndingAltitude() > getEndingAltitude()
				) {
				return false;
			}
			
			if (center.distance(other.center, false) + other.radius > radius) {
				return false;
			}
			
			return true;
		}
		
		return super.containsTotally(bounds);
	}
	
	@Override
	public boolean intersects(final ThreeDBounds bounds) {
		if (bounds instanceof ThreeDCircularBounds) {
			ThreeDCircularBounds other = (ThreeDCircularBounds) bounds;
			if (
					other.getEndingAltitude() < getStartingAltitude() || 
					other.getStartingAltitude() > getEndingAltitude()) {
				return false;
			}
			
			if (center.distance(other.center, false) - other.radius > radius) {
				return false;
			}
			
			return true;
		}
		
		return super.containsPartially(bounds);
	}	
	
	@Override
	public String toString() {
		return String.format("ThreeDCircularBounds[%s, center=%s, radius=%s, height=%s]", 
				super.toString(),
				center, 
				radius, 
				height
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

		ThreeDCircularBounds other = (ThreeDCircularBounds) obj;
		return center.equals(other.center) && 
			   Double.compare(other.radius, radius) == 0 && 
			   Double.compare(other.height, height) == 0;
	}
}
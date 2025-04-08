package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;

public abstract class ThreeDBounds {
	
	public abstract boolean contains(Position coordinate);
	public abstract boolean intersects(ThreeDBounds bounds);
	public abstract boolean contains(ThreeDBounds bounds);
	public abstract ThreeDBoundingBox getBoundingBox();
	public abstract double getStartingAltitude();
	public abstract double getEndingAltitude();
	
	protected final boolean containsTotally(final ThreeDBounds other) {
		assert other != null;
		
		double lowerBoundAltitude = getStartingAltitude();
		double upperBoundAltitude = getEndingAltitude();
		
		if (lowerBoundAltitude > other.getStartingAltitude() || upperBoundAltitude < other.getEndingAltitude()) {
			return false;
		}
		
		for (Position position : other.getBoundingBox().getPositions()) {
			if (!contains(position)) {
				return false;
			}
		}
		return true;
	}
	
	protected final boolean containsPartially(final ThreeDBounds other) {
		return getBoundingBox().intersect(other.getBoundingBox());
	}
	
	public final boolean contains(final ThreeDBounds other, final boolean partially) {
		assert other != null;
		assert other.getBoundingBox() != null;
		
		if (partially) {
			return intersects(other);
		}    
		return contains(other);	
	}	
	
	@Override
	public String toString() {
		return String.format("ThreeDBounds[]");
	}
}
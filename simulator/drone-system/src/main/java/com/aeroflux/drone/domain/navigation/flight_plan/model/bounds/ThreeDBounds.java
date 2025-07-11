package com.aeroflux.drone.domain.navigation.flight_plan.model.bounds;

import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.model.shape.GeoShape;
import com.aeroflux.drone.domain.navigation.flight_plan.model.ThreeDBoundingBox;

public abstract class ThreeDBounds {
	
	private static enum SpatialRelation {
		WITHIN, CONTAINS, INTERSECTS, DISJOINT, OVERLAPS
	}
	
	public abstract ThreeDBoundingBox getBoundingBox();
	public abstract double getStartingAltitude();
	public abstract double getEndingAltitude();
	public abstract GeoShape getGeoShape();
	
	public boolean contains(final Position coordinate) {
		assert coordinate != null;
		
		if (
				coordinate.getAltitude() < getStartingAltitude() ||
				coordinate.getAltitude() > getEndingAltitude()
			) {
			return false;
		}
		
		GeoShape shape = this.getGeoShape();
		return shape.contains(coordinate);
	}
	
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
		return getBoundingBox().overlaps(other.getBoundingBox());
	}
	
	public final boolean contains(final ThreeDBounds other, final boolean partially) {		
		if (partially) {
			return overlaps(other);
		}    
		return contains(other);	
	}	
	
	private boolean relates(final ThreeDBounds bounds, final SpatialRelation relation) {
		assert bounds != null;

		GeoShape shape1 = this.getGeoShape();
		GeoShape shape2 = bounds.getGeoShape();

		switch (relation) {
		case WITHIN:
			return shape1.within(shape2);
		case CONTAINS:
			return shape1.contains(shape2);
		case INTERSECTS:
			return shape1.intersects(shape2);
		case DISJOINT:
			return shape1.disjoint(shape2);
		case OVERLAPS:
			return shape1.overlaps(shape2);
		default:
			throw new IllegalArgumentException("Unknown relation: " + relation);
		}
	}
	
	public boolean contains(final ThreeDBounds other) {
		if (
				other.getStartingAltitude() < getStartingAltitude() || 
				other.getEndingAltitude() > getEndingAltitude()
			) {
			return false;
		}
		
		return relates(other, SpatialRelation.CONTAINS);
	}
	
	public boolean overlaps(final ThreeDBounds other) {
		if (
				other.getStartingAltitude() > getEndingAltitude() || 
				other.getEndingAltitude() < getStartingAltitude()
			) {
			return false;
		}
		
		return relates(other, SpatialRelation.OVERLAPS);
	}
		
	@Override
	public String toString() {
		return String.format("ThreeDBounds[]");
	}	
}
package com.aeroflux.drone.domain.model.shape;

import com.aeroflux.drone.domain.geo.GeoCalculatorFactory;
import com.aeroflux.drone.domain.model.Coordinate;

public abstract class GeoShape {
	
	public boolean contains(final Coordinate c) {
		return GeoCalculatorFactory.getGeoShapeCalculator().contains(this, c);
	}
	
	public boolean contains(final GeoShape other) {
		return GeoCalculatorFactory.getGeoShapeCalculator().contains(this, other);
	}
	
	public boolean within(final GeoShape other) {
		return GeoCalculatorFactory.getGeoShapeCalculator().within(this, other);
	}
	
	public boolean intersects(final GeoShape other) {
		return GeoCalculatorFactory.getGeoShapeCalculator().intersects(this, other);
	}
	
	public boolean disjoint(final GeoShape other) {
		return GeoCalculatorFactory.getGeoShapeCalculator().disjoint(this, other);
	}
	
	public boolean overlaps(final GeoShape other) {
		return GeoCalculatorFactory.getGeoShapeCalculator().overlaps(this, other);
	}
}
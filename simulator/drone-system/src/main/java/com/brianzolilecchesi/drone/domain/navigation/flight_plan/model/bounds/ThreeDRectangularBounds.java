package com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.bounds;

import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.shape.GeoRectangle;
import com.brianzolilecchesi.drone.domain.model.shape.GeoShape;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.ThreeDBoundingBox;

public class ThreeDRectangularBounds extends ThreeDBounds {

	private ThreeDBoundingBox boundingBox;

	public ThreeDRectangularBounds(final ThreeDBoundingBox boundingBox) {
		setBoundingBox(boundingBox);
	}

	public ThreeDRectangularBounds(final Position center, double latWidth, double lonWidth, double height) {
		this(new ThreeDBoundingBox(
				center.move(latWidth / 2, -lonWidth / 2, height / 2), // Top North West
				center.move(latWidth / 2, -lonWidth / 2, Math.max(-height / 2, -center.getAltitude())), // Bottom North West

				center.move(latWidth / 2, lonWidth / 2, height / 2), // Top North East
				center.move(latWidth / 2, lonWidth / 2, Math.max(-height / 2, -center.getAltitude())), // Bottom North East

				center.move(-latWidth / 2, lonWidth / 2, height / 2), // Top South East
				center.move(-latWidth / 2, lonWidth / 2, Math.max(-height / 2, -center.getAltitude())), // Bottom South East

				center.move(-latWidth / 2, -lonWidth / 2, height / 2), // Top South West
				center.move(-latWidth / 2, -lonWidth / 2, Math.max(-height / 2, -center.getAltitude())) // Bottom South West
		));
	}

	public ThreeDRectangularBounds(final Position center, double width, double height) {
		this(center, width, width, height);
	}
	
	
	
	public ThreeDRectangularBounds(final Position p1, final Position p2, double delta) {
		this(new ThreeDBoundingBox(p1, p2, delta));
	}

	@Override
	public double getStartingAltitude() {
		return boundingBox.getLowerAltitude();
	}

	@Override
	public double getEndingAltitude() {
		return boundingBox.getUpperAltitude();
	}

	@Override
	public ThreeDBoundingBox getBoundingBox() {
		return boundingBox;
	}

	private void setBoundingBox(final ThreeDBoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	@Override
	public GeoShape getGeoShape() {
		ThreeDBoundingBox bbox = this.getBoundingBox();
		Position tne = bbox.getTNE(), 
				 bne = bbox.getBNE(),
				 tsw = bbox.getTSW(),
				 bsw = bbox.getBSW();
		
		Coordinate ne = new Coordinate(
				Math.max(tne.getLatitude(), bne.getLatitude()),
				Math.max(tne.getLongitude(), bne.getLongitude())
				);
		
		Coordinate sw = new Coordinate(
				Math.min(tsw.getLatitude(), bsw.getLatitude()),
				Math.min(tsw.getLongitude(), bsw.getLongitude())
				);
		
		return new GeoRectangle(ne, sw);
	}

	@Override
	public String toString() {
		return String.format("ThreeDRectangularBounds[%s, boundingBox=%s]", super.toString(), boundingBox);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		ThreeDRectangularBounds other = (ThreeDRectangularBounds) obj;
		return boundingBox.equals(other.boundingBox);
	}
	
	@Override
	public int hashCode() {
		return boundingBox.hashCode();
	}
}
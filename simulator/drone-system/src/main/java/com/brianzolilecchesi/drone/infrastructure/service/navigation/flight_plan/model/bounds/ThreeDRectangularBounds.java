package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;

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
	public boolean contains(final Position coordinate) {
		return boundingBox.contains(coordinate);
	}
	
	@Override
	public boolean contains(ThreeDBounds other) {
		return boundingBox.contains(other.getBoundingBox());
	}
	
	@Override
	public boolean intersects(final ThreeDBounds other) {
		return boundingBox.intersect(other.getBoundingBox());
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
}
package com.aeroflux.drone.domain.navigation.flight_plan.model.zone;

import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDBounds;

public class Geozone extends Zone {
	
	private ThreeDBounds bounds;

	public Geozone(String id, final ThreeDBounds bounds) {
		super(id);
		this.bounds = bounds;
	}

	@Override
	public ThreeDBounds getBounds() {
		return bounds;
	}
	
	@Override
	public String toString() {
		return String.format("Geozone[%s, bounds=%s]", super.toString(), bounds.toString());
	}
}
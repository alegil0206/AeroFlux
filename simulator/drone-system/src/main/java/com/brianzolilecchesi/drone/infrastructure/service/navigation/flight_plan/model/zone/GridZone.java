package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDBounds;

public class GridZone extends Zone {
	
	private ThreeDBounds bounds;

	public GridZone(String id, ThreeDBounds bounds) {
		super(id);
		this.bounds = bounds;
	}
	
	@Override
	public ThreeDBounds getBounds() {
		return bounds;
	}
	
	@Override
	public String toString() {
		return String.format("GridZone[%s]", super.toString());
	}
}
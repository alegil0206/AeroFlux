package com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.bounds.ThreeDBounds;

public class RainZone extends Zone {
	private final ThreeDBounds bounds;

	
	public RainZone(String id, ThreeDBounds bounds) {
		super(id);
		this.bounds = bounds;
	}

	@Override
	public ThreeDBounds getBounds() {
		return bounds;
	}
	
	@Override
	public String toString() {
		return String.format("RainZone[%s]", super.toString());
	}
}
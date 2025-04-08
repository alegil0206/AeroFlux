package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDBounds;

//TODO: Implement this class
public class WeatherZone extends Zone {
	
	public WeatherZone(String id) {
		super(id);
	}

	@Override
	public ThreeDBounds getBounds() {
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("WeatherZone[%s]", super.toString());
	}
}
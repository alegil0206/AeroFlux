package com.aeroflux.drone.domain.navigation.exception.flight_plan.model.graph;

import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Zone;

public class IllegalLinearPathException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final Zone zone;
	
	public IllegalLinearPathException(final Zone zone) {
		super(String.format("Zone %s found in linear path", zone.getId()));
		this.zone = zone;
	}
	
	public Zone getZone() {
		return zone;
	}
}
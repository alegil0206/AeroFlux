package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.FlightPlan;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.CellGraph;

public abstract class PathFinder {

	public PathFinder() {
	}

	public abstract FlightPlan computePath(final CellGraph graph);
	public abstract String algorithm();

	@Override
	public String toString() {
		return String.format(
				"PathFinder[targetSensitivity=%s, acceptableSensitivity=%s, maxTimeAcceptable=%s, maxTime=%s]");
	}
}
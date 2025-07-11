package com.aeroflux.drone.domain.navigation.flight_plan.model.graph.finder;

import com.aeroflux.drone.domain.navigation.flight_plan.model.FlightPlan;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.CellGraph;

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
package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.FlightPlan;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.CellGraph;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Cell;

public class LinearFinder extends PathFinder {
	
	public LinearFinder() {
	}
	
	@Override
	public String algorithm() {
		return "Linear";
	}

	@Override
	public FlightPlan computePath(final CellGraph graph) {
		assert graph != null;
		assert graph.getSource() != null;
		assert graph.getDest() != null;
		
		List<Cell> path = new ArrayList<>();
		path.add(graph.getSource());
		path.add(graph.getDest());
		
		return new FlightPlan(path);	
	}
}
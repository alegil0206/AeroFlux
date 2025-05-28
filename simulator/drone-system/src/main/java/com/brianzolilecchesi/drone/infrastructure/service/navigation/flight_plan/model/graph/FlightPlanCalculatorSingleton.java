package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder.BidirectionalDjikstraFinder;

public class FlightPlanCalculatorSingleton {
	
	private static volatile FlightPlanCalculator instance;

	public static FlightPlanCalculator getInstance() {
		if (instance == null) {
			synchronized (FlightPlanCalculator.class) {
				if (instance == null) {
					instance = new FlightPlanCalculator(
							new BidirectionalDjikstraFinder(),
					        new CellGraphBuilder()
					        );
				}
			}
		}
		return instance;
	}
	
	static void resetInstance() {
	    instance = null;
	}
	
	static void setInstance(FlightPlanCalculator testInstance) {
	    instance = testInstance;
	}
}
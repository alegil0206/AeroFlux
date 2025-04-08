package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.FlightNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder.BidirectionalDjikstraFinder;

public class FlightPlanCalculatorSingleton {
	
	private static FlightPlanCalculator instance;

	public static FlightPlanCalculator getInstance() {
		if (instance == null) {
			synchronized (FlightPlanCalculator.class) {
				if (instance == null) {
					instance = new FlightPlanCalculator(
							FlightNavigationService.TARGET_SENSITIVITY,
							FlightNavigationService.ACCEPTABLE_SENSITIVITY,
							FlightNavigationService.MAX_TIME_ACCEPTABLE,
							FlightNavigationService.MAX_TIME,
							new BidirectionalDjikstraFinder(),
					        new CellGraphBuilder()
					        );
				}
			}
		}
		return instance;
	}
}
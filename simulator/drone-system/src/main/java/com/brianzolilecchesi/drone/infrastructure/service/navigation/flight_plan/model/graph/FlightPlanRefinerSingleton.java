package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

public class FlightPlanRefinerSingleton {
	
	private static FlightPlanRefiner instance;

	public static FlightPlanRefiner getInstance() {
		if (instance == null) {
			synchronized (FlightPlanRefiner.class) {
				if (instance == null) {
					instance = new FlightPlanRefiner();
				}
			}
		}
		return instance;
	}
}
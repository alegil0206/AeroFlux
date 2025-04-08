package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.geo.GeoCalculator;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.geo.GeoCalculatorSingleton;

public class FlightPlanRefiner {
	
	public FlightPlanRefiner() {
		
	}
	
	public List<Position> refine(final List<Position> positions, double d) {
		assert positions != null;
		assert positions.size() > 0;
		
		if (positions.size() == 1)
			return positions;
		
		GeoCalculator geo = GeoCalculatorSingleton.INSTANCE.getInstance();
				
		boolean changed = true;
		
		while (changed) {
			changed = false;
			for (int i = 0; i < positions.size() - 1; ++i) {
				Position current = positions.get(i);
	            Position next = positions.get(i + 1);
	            double distance = geo.distance(current, next, false);
	            
	            if (distance > d) {
	            	double bearing = geo.bearing(current, next);
	            	double altitude = current.getAltitude();
            		if (altitude == 0) altitude = next.getAltitude();
            		if (altitude == 0) altitude = FlightPlanCalculator.DEFAULT_HEIGHT / 2;
	            	
					Position midpoint = new Position(
							geo.moveByBearing(current, bearing, distance / 2),
							altitude
							);
							
					positions.add(i + 1, midpoint);
					++i;
					changed = true;
	            }
			}			
		}
		
		return positions;
	}
}
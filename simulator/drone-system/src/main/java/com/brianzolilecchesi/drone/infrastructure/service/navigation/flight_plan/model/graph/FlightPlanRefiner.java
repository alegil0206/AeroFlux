package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import java.util.List;

import com.brianzolilecchesi.drone.domain.geo.GeoCalculatorFactory;
import com.brianzolilecchesi.drone.domain.geo.GeoDistanceCalculator;
import com.brianzolilecchesi.drone.domain.model.Position;

public class FlightPlanRefiner {
	
	public FlightPlanRefiner() {
	}
	
	public static List<Position> refineLinearPath(final List<Position> positions, double d) {
		assert positions != null;
		assert positions.size() == 2;
		
		Position current = positions.get(0);
		Position target = positions.remove(positions.size() - 1);
		
		GeoDistanceCalculator geo = GeoCalculatorFactory.getGeoDistanceCalculator();
		
		while (geo.distance(current, target, false) > d) {	
			double altitude = current.getAltitude();
			if (altitude == 0)
				altitude = FlightPlanCalculator.DEFAULT_HEIGHT / 2;
			
			double bearing = geo.bearing(current, target);
			Position next = new Position(geo.moveByBearing(current, bearing, d), altitude);

			positions.add(next);
			current = next;
			
		}	
		positions.add(target);
	
		return positions;
	}
	
	public static List<Position> refine(final List<Position> positions, double d) {
		assert positions != null;
		assert positions.size() > 0;
		
		if (positions.size() == 1)
			return positions;
		
		if (positions.size() == 2) {
			return refineLinearPath(positions, d);
		}
		
		GeoDistanceCalculator geo = GeoCalculatorFactory.getGeoDistanceCalculator();
				
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
package com.aeroflux.drone.domain.navigation.flight_plan.model.zone.adapter;

import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.navigation.flight_plan.model.ThreeDBoundingBox;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.GridZone;

public class GridZoneAdapter {
		
	private static final double MAX_ALTITUDE = 120.0;
	private static final double MIN_ALTITUDE = 0.0;

	public GridZoneAdapter() {
	}
	
	
	public GridZone build(final Position start, final Position destination, double delta) {
		Position northest = start.getLatitude() >= destination.getLatitude() ? start : destination;
		northest = northest.move(delta, 0, 0);
		Position southest = start.getLatitude() < destination.getLatitude() ? start : destination;
		southest = southest.move(-delta, 0, 0);
		
		Position eastest = start.getLongitude() >= destination.getLongitude() ? start : destination;
		eastest = eastest.move(0, delta, 0);
		Position westest = start.getLongitude() < destination.getLongitude() ? start : destination;	
		westest = westest.move(0, -delta, 0);
				
		return new GridZone("GridZone", new ThreeDRectangularBounds(
				new ThreeDBoundingBox(
						new Position(northest.getLatitude(), westest.getLongitude(), MAX_ALTITUDE),	// tno	
						new Position(northest.getLatitude(), westest.getLongitude(), MIN_ALTITUDE),			// bno
						
						new Position(northest.getLatitude(), eastest.getLongitude(), MAX_ALTITUDE),	// tne
						new Position(northest.getLatitude(), eastest.getLongitude(), MIN_ALTITUDE),			// bne
						
						new Position(southest.getLatitude(), eastest.getLongitude(), MAX_ALTITUDE),	// tse
						new Position(southest.getLatitude(), eastest.getLongitude(), MIN_ALTITUDE),			// bse
						
						new Position(southest.getLatitude(), westest.getLongitude(), MAX_ALTITUDE),	// tso
						new Position(southest.getLatitude(), westest.getLongitude(), MIN_ALTITUDE)			// bso
						)
				));
	}
	
	@Override
	public String toString() {
		return "GridZoneAdapter[MAX_ALTITUDE=%s]".formatted(MAX_ALTITUDE);
	}

}

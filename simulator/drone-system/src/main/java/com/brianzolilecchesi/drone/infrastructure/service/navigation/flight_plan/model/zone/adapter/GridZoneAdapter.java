package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.GeozoneNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.GridZone;

public class GridZoneAdapter {
		
	private final double maxAltitude;
	
	public GridZoneAdapter(double maxAltitude) {
		assert maxAltitude > 0;
		this.maxAltitude = maxAltitude;
	}
	
	public GridZoneAdapter() {
		this(GeozoneNavigationService.MAX_ALTITUDE);
	}
	
	public double getMaxAltitude() {
		return maxAltitude;
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
						new Position(northest.getLatitude(), westest.getLongitude(), maxAltitude),	// tno	
						new Position(northest.getLatitude(), westest.getLongitude(), 0.0),			// bno
						
						new Position(northest.getLatitude(), eastest.getLongitude(), maxAltitude),	// tne
						new Position(northest.getLatitude(), eastest.getLongitude(), 0.0),			// bne
						
						new Position(southest.getLatitude(), eastest.getLongitude(), maxAltitude),	// tse
						new Position(southest.getLatitude(), eastest.getLongitude(), 0.0),			// bse
						
						new Position(southest.getLatitude(), westest.getLongitude(), maxAltitude),	// tso
						new Position(southest.getLatitude(), westest.getLongitude(), 0.0)			// bso
						)
				));
	}
	
	@Override
	public String toString() {
		return "GridZoneAdapter[maxAltitude=%s]".formatted(maxAltitude);
	}

}

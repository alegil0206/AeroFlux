package com.aeroflux.drone.domain.navigation.flight_plan.model.zone.adapter;

import com.aeroflux.drone.domain.model.Coordinate;
import com.aeroflux.drone.domain.model.GeoZone;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.navigation.flight_plan.model.ThreeDBoundingBox;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDCircularBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Geozone;

public class GeozoneAdapter {
	
	private static final String CIRCULAR = "CIRCULAR";
	private static final String POLYGONAL = "POLYGONAL";
		
	public GeozoneAdapter() {
	}
	
	public Geozone adapt(GeoZone geoZone) {
		assert geoZone != null;
		assert geoZone.getType() != null;
		
		switch (geoZone.getType()) {
		case CIRCULAR:
			return adaptCircular(geoZone);
		case POLYGONAL:
			return adaptPolygonal(geoZone);
		}
		
		throw new IllegalArgumentException("Unknown GeoZone type: " + geoZone.getType());
	}
	
	private Geozone adaptCircular(GeoZone geoZone) {
		assert geoZone.getId() != null;
		assert geoZone.getAltitudeLimitInferior() != null;
		assert geoZone.getAltitudeLimitSuperior() != null;
		assert geoZone.getRadius() != null;
		assert geoZone.getLatitude() != null;
		assert geoZone.getLongitude() != null;
		
		double centerAltitude = (geoZone.getAltitudeLimitSuperior() + geoZone.getAltitudeLimitInferior()) / 2;
		double geozoneHeight = (geoZone.getAltitudeLimitSuperior() - centerAltitude) * 2;
				
		ThreeDBounds bounds = new ThreeDCircularBounds(
				new Position(geoZone.getLatitude(), geoZone.getLongitude(), centerAltitude),
				geoZone.getRadius(),
				geozoneHeight
				);
		
		return new Geozone(geoZone.getId(), bounds);
	}
	
	private Geozone adaptPolygonal(GeoZone geoZone) {
		assert geoZone.getId() != null;
		assert geoZone.getCoordinates() != null;
		assert geoZone.getCoordinates().size() > 2;
		assert geoZone.getAltitudeLimitInferior() != null;
		assert geoZone.getAltitudeLimitSuperior() != null;
		
		double lowest = geoZone.getAltitudeLimitInferior(),
			   uppest = geoZone.getAltitudeLimitSuperior();
			   
		double northest = -Double.MAX_VALUE,
			   southest = Double.MAX_VALUE,
			   eastest = -Double.MAX_VALUE,
			   westest = Double.MAX_VALUE;
		
		Double latitude = null, longitude = null; 
		for (Coordinate c : geoZone.getCoordinates()) {
			longitude = c.getLongitude();
			latitude = c.getLatitude();
			northest = Math.max(northest, latitude);
			southest = Math.min(southest, latitude);
			eastest = Math.max(eastest, longitude);
			westest = Math.min(westest, longitude);
		}
		
		ThreeDBounds bounds = new ThreeDRectangularBounds(
				new ThreeDBoundingBox(
						new Position(northest, westest, uppest), 	// Top North West
						new Position(northest, westest, lowest),	// Bottom North West
						
						new Position(northest, eastest, uppest),	// Top North East
						new Position(northest, eastest, lowest),	// Bottom North East
						
						new Position(southest, eastest, uppest),	// Top South East
						new Position(southest, eastest, lowest),	// Bottom South East
						
						new Position(southest, westest, uppest),	// Top South West
						new Position(southest, westest, lowest)		// Bottom South West
						)
				);
		
		return new Geozone(geoZone.getId(), bounds);
	}
}
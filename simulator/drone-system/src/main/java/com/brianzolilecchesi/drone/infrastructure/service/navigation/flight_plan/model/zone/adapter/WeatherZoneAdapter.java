package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.WeatherNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDBounds;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.RainZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;

public class WeatherZoneAdapter {
	private final double maxAltitude;
	private final double minAltitude;
	
	public WeatherZoneAdapter(double maxAltitude, double minAltitude) {
		assert maxAltitude > 0;
		assert minAltitude >= 0;
		this.maxAltitude = maxAltitude;
		this.minAltitude = minAltitude;
	}
	public WeatherZoneAdapter() {
		this(WeatherNavigationService.MAX_ALTITUDE, WeatherNavigationService.MIN_ALTITUDE);
	}

	public Zone adapt(RainCell weatherData) {
		assert weatherData != null;
		assert weatherData.getCoordinates() != null;
		assert weatherData.getCoordinates().size() > 2;

		double lowest = minAltitude,
			   uppest = maxAltitude;
			   
		double northest = -Double.MAX_VALUE,
			   southest = Double.MAX_VALUE,
			   eastest = -Double.MAX_VALUE,
			   westest = Double.MAX_VALUE;
		
		Double latitude = null, longitude = null; 
		for (double[] c : weatherData.getCoordinates()) {
			longitude = c[1];
			latitude = c[0];
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
		
		return new RainZone("rainCell", bounds);
	}

	public double getMaxAltitude() {
		return maxAltitude;
	}
	
	public double getMinAltitude() {
		return minAltitude;
	}

	
}
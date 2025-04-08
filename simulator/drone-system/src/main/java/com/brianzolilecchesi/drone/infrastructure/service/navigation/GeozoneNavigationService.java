package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculatorSingleton;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Geozone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class GeozoneNavigationService {
	
	public static final double MAX_ALTITUDE = 120.0;
	
	public GeozoneNavigationService() {
	}
	
	public boolean addGeoZone(final GeoZone geoZone) {
		Zone geozone = null;
		
		try {
			geozone = ZoneAdapterFacade.getInstance().adapt(geoZone);		
		} catch (Exception e) {
			System.err.println("Error adapting GeoZone: " + e.getMessage());
			return false;
		}
		
		return FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones().add(geozone);
	}
	
	public boolean addGeoZones(final List<GeoZone> geoZones) {
		assert geoZones != null;
		
		boolean result = true;
		for (GeoZone zone : geoZones) {
            result = result && addGeoZone(zone);
        }
		
		return result;
	}
	
	public boolean removeGeoZone(final GeoZone geoZone) {
		assert geoZone != null;
		
		List<Zone> zones = FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones();
		int i = 0;
		
		while (i < zones.size()) {
			Zone zone = zones.get(i);
			if (
					zone instanceof Geozone && 
					zone.getId().equals(geoZone.getId())
				) {
				zones.remove(i);
				return true;
			}
			
			i++;
		}
		
		return false;
	}
	
	public void clearGeoZones() {
		List<Zone> newZones = new ArrayList<>();
		List<Zone> zones = FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones();		
		
		for (Zone zone : zones) {
			if (!(zone instanceof Geozone)) {
				newZones.add(zone);
			}
		}
		
		FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().setZones(newZones);
	}
}
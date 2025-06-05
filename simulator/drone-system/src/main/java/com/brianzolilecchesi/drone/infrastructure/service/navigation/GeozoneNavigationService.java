package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Geozone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class GeozoneNavigationService {
	
	public static final double MAX_ALTITUDE = 120.0;

	private FlightPlanCalculator flightPlanCalculator;
	private ZoneAdapterFacade zoneAdapterFacade;
	
	public GeozoneNavigationService(FlightPlanCalculator flightPlanCalculator, ZoneAdapterFacade zoneAdapterFacade) {
        this.flightPlanCalculator = flightPlanCalculator;
        this.zoneAdapterFacade = zoneAdapterFacade;
    }
	
	public boolean add(final GeoZone geoZone) {
		Zone geozone = null;
		
		try {
			geozone = zoneAdapterFacade.adapt(geoZone);		
		} catch (Exception e) {
			System.err.println("Error adapting GeoZone: " + e.getMessage());
			return false;
		}
		
		return flightPlanCalculator.getCellGraphBuilder().getZones().add(geozone);
	}
	
	public boolean add(final List<GeoZone> geoZones) {
		assert geoZones != null;
		
		boolean result = true;
		for (GeoZone zone : geoZones) {
            result = result && add(zone);
        }
		
		return result;
	}
	
	public boolean remove(final GeoZone geoZone) {
		assert geoZone != null;
		
		List<Zone> zones = flightPlanCalculator.getCellGraphBuilder().getZones();
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

	public boolean remove(final Set<GeoZone> geoZones) {
		assert geoZones != null;
		
		if (geoZones.isEmpty()) {
			return true;
		}
		
		Set<String> geoZoneIds = new HashSet<>();
		for (GeoZone zone : geoZones) {
			geoZoneIds.add(zone.getId());
		}
		
		List<Zone> previousZones = flightPlanCalculator.getCellGraphBuilder().getZones();
		List<Zone> updatedZones = new ArrayList<>();
		int i = 0;
		
		while (i < previousZones.size()) {
			Zone zone = previousZones.get(i);
			if (!(zone instanceof Geozone && geoZoneIds.contains(zone.getId()))) {
				updatedZones.add(zone);
			}
			
			i++;
		}
		
		flightPlanCalculator.getCellGraphBuilder().setZones(updatedZones);
		boolean removedAll = previousZones.size() - updatedZones.size() == geoZones.size();
		
		return removedAll;
	}
	
	public void clear() {
		List<Zone> newZones = new ArrayList<>();
		List<Zone> zones = flightPlanCalculator.getCellGraphBuilder().getZones();		
		
		for (Zone zone : zones) {
			if (!(zone instanceof Geozone)) {
				newZones.add(zone);
			}
		}
		
		flightPlanCalculator.getCellGraphBuilder().setZones(newZones);
	}
}
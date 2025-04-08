package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter;

import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.WeatherZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;

public class ZoneAdapterFacade {
	
	private static ZoneAdapterFacade INSTANCE;
	
	public static ZoneAdapterFacade getInstance() {
	    if (INSTANCE == null) {
	        synchronized (ZoneAdapterFacade.class) {
	            if (INSTANCE == null) {
	                INSTANCE = new ZoneAdapterFacade(new GridZoneAdapter(), new GeozoneAdapter(), new WeatherZoneAdapter());
	            }
	        }
	    }
	    return INSTANCE;
	}
	
	private final GridZoneAdapter gridAdapter;
	private final GeozoneAdapter geozoneAdapter;
	private final WeatherZoneAdapter weatherAdapter;
	
	private ZoneAdapterFacade(GridZoneAdapter gridAdapter, GeozoneAdapter geozoneAdapter, WeatherZoneAdapter weatherAdapter) {
		this.gridAdapter = gridAdapter;
		this.geozoneAdapter = geozoneAdapter;
		this.weatherAdapter = weatherAdapter;
	}
	
	public GridZoneAdapter getGridAdapter() {
		return gridAdapter;
	}
	
	public GeozoneAdapter getGeozoneAdapter() {
		return geozoneAdapter;
	}
	
	public WeatherZoneAdapter getWeatherAdapter() {
		return weatherAdapter;
	}
	
	public Zone adapt(Object zoneToAdapt) {
		Zone zone = null;
		if (zoneToAdapt instanceof GeoZone) {
			zone = geozoneAdapter.adapt((GeoZone) zoneToAdapt);
		} else if (zone instanceof WeatherZone) {
			zone = weatherAdapter.adapt();
		}

		if (zone == null)
			throw new IllegalArgumentException("Unknown zone type: " + zoneToAdapt);
		
		return zone;
	}
	
	@Override
	public String toString() {
		return "ZoneAdapterFacade[gridAdapter=%s, geozoneAdapter=%s, weatherAdapter=%s]".formatted(
				gridAdapter,
				geozoneAdapter, 
				weatherAdapter
				);
	}
}
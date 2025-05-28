package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter;

import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;

public class ZoneAdapterFacade {
	
	private static ZoneAdapterFacade INSTANCE;
	
	public static ZoneAdapterFacade getInstance() {
	    if (INSTANCE == null) {
	        synchronized (ZoneAdapterFacade.class) {
	            if (INSTANCE == null) {
	                INSTANCE = new ZoneAdapterFacade(new GridZoneAdapter(), new GeozoneAdapter(), new WeatherZoneAdapter(), new DronePositionAdapter());
	            }
	        }
	    }
	    return INSTANCE;
	}
	
	private final GridZoneAdapter gridAdapter;
	private final GeozoneAdapter geozoneAdapter;
	private final WeatherZoneAdapter weatherAdapter;
	private final DronePositionAdapter dronePositionAdapter;
	
	private ZoneAdapterFacade(GridZoneAdapter gridAdapter, GeozoneAdapter geozoneAdapter, WeatherZoneAdapter weatherAdapter, DronePositionAdapter dronePositionAdapter) {
		this.gridAdapter = gridAdapter;
		this.geozoneAdapter = geozoneAdapter;
		this.weatherAdapter = weatherAdapter;
		this.dronePositionAdapter = dronePositionAdapter;
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

	public DronePositionAdapter getDronePositionAdapter() {
		return dronePositionAdapter;
	}
	
	public Zone adapt(Object zoneToAdapt) {
		Zone zone = null;
		if (zoneToAdapt instanceof GeoZone) {
			zone = geozoneAdapter.adapt((GeoZone) zoneToAdapt);
		} else if (zoneToAdapt instanceof RainCell) {
			zone = weatherAdapter.adapt((RainCell) zoneToAdapt);
		} else if (zoneToAdapt instanceof NearbyDroneStatus) {
			zone = dronePositionAdapter.adapt((NearbyDroneStatus) zoneToAdapt);
		}

		if (zone == null)
			throw new IllegalArgumentException("Unknown zone type: " + zoneToAdapt);
		
		return zone;
	}
	
	@Override
	public String toString() {
		return "ZoneAdapterFacade[gridAdapter=%s, geozoneAdapter=%s, weatherAdapter=%s, dronePositionAdapter=%s ]".formatted(
				gridAdapter,
				geozoneAdapter, 
				weatherAdapter,
				dronePositionAdapter
				);
	}
}
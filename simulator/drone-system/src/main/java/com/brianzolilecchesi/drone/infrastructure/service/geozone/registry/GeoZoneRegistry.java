package com.brianzolilecchesi.drone.infrastructure.service.geozone.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brianzolilecchesi.drone.domain.model.GeoZone;

public class GeoZoneRegistry {
	
	private Map<String, GeoZone> geoZones;
	
	private static Map<String, GeoZone> fromList(List<GeoZone> geoZones) {
		assert geoZones != null;
		
		Map<String, GeoZone> map = new HashMap<>();
		for (GeoZone geoZone: geoZones) {
			map.put(geoZone.getId(), geoZone);
		}
		
		return map;
	}
	
	public GeoZoneRegistry(Map<String, GeoZone> geoZones) {
		assert geoZones != null;
		
		this.geoZones = geoZones;
	}
	
	public GeoZoneRegistry(List<GeoZone> geoZones) {
		this(fromList(geoZones));
	}
	
	public GeoZoneRegistry() {
		this(new HashMap<>());
	}
	
	public GeoZone get(String id) {
		return geoZones.get(id);
	}
	
	public Map<String, GeoZone> getAll() {
		return geoZones;
	}
	
	public void set(Map<String, GeoZone> geoZones) {
		assert geoZones != null;
		
		this.geoZones = geoZones;
	}
	
	public GeoZone add(final GeoZone geoZone) {
		assert geoZone != null;
		
		return this.geoZones.put(geoZone.getId(), geoZone);
	}
	
	public void addAll(final List<GeoZone> geoZones) {
		assert geoZones != null;
		
		for (GeoZone geoZone : geoZones) {
			assert geoZone != null;
			this.geoZones.put(geoZone.getId(), geoZone);
		}
	}
	
	public GeoZone remove(final GeoZone geoZone) {
		return this.geoZones.remove(geoZone.getId());
	}
	
	public void clear() {
		this.geoZones.clear();
	}
	
	@Override
	public String toString() {
		return String.format("GeozoneRegistry[geoZones=%s]", geoZones);
	}

}

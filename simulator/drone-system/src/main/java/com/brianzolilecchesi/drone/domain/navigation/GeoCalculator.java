package com.brianzolilecchesi.drone.domain.navigation;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Position;

import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.adapter.ZoneAdapter;

public class GeoCalculator {
    private final ZoneAdapter zoneAdapter;

    public GeoCalculator(ZoneAdapter zoneAdapter) {
        this.zoneAdapter = zoneAdapter;
    }

    public GeoCalculator() {
        this.zoneAdapter = new ZoneAdapter();
    }

    public boolean isPointInZone(Position position, Object zone) {
        return zoneAdapter.adapt(zone).getBounds().contains(position);
    }

    public boolean isPointInZone(Position position, List<Object> zones) {
        for (Object zone : zones) {
            if (isPointInZone(position, zone)) {
                return true;
            }
        }
        return false;
    }
}

package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDBounds;

public class DroneZone extends Zone {

    private final ThreeDBounds bounds;

    public DroneZone(String id, ThreeDBounds bounds) {
        super(id);
        this.bounds = bounds;
    }

    @Override
    public ThreeDBounds getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        return String.format("DroneZone[%s, bounds=%s]", super.toString(), bounds.toString());
    }
    
}

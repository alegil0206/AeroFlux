package com.aeroflux.drone.domain.navigation.flight_plan.model.zone.adapter;

import com.aeroflux.drone.domain.model.NearbyDroneStatus;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.DroneZone;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Zone;

public class DronePositionAdapter {
    
    public DronePositionAdapter() {
    }

    public Zone adapt(NearbyDroneStatus drone) {
        assert drone != null;
        ThreeDBounds bounds = new ThreeDRectangularBounds(drone.getPosition(), 1.0, 1.0);
        Zone zone = new DroneZone(drone.getDroneId(), bounds);
        return zone;
    }
}

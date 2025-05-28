package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculatorSingleton;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.DroneZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class DroneZoneNavigationService {
    
    public DroneZoneNavigationService() {}

    public boolean add(final NearbyDroneStatus drone) {
        assert drone != null;

        Zone droneZone = null;
        try {
            droneZone = ZoneAdapterFacade.getInstance().adapt(drone);
        } catch (Exception e) {
            return false;
        }
        return FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones().add(droneZone);
    }

    public void clear() {

        List<Zone> newZones = new ArrayList<>();
        List<Zone> zones = FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones();

        for (Zone zone : zones) {
            if (!(zone instanceof DroneZone)) {
                newZones.add(zone);
            }
        }
        FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().setZones(newZones);
    }

}

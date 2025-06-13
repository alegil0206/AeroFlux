package com.brianzolilecchesi.drone.domain.navigation.zoneService;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.DroneZone;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.adapter.ZoneAdapter;

public class DroneZoneNavigationService {

    private final FlightPlanCalculator flightPlanCalculator;
    private final ZoneAdapter zoneAdapterFacade;
    
    public DroneZoneNavigationService(FlightPlanCalculator flightPlanCalculator, ZoneAdapter zoneAdapterFacade) {
        this.flightPlanCalculator = flightPlanCalculator;
        this.zoneAdapterFacade = zoneAdapterFacade;
    }

    public boolean add(final NearbyDroneStatus drone) {
        assert drone != null;

        Zone droneZone = null;
        try {
            droneZone = zoneAdapterFacade.adapt(drone);
        } catch (Exception e) {
            return false;
        }
        return flightPlanCalculator.getCellGraphBuilder().getZones().add(droneZone);
    }

    public void clear() {

        List<Zone> newZones = new ArrayList<>();
        List<Zone> zones = flightPlanCalculator.getCellGraphBuilder().getZones();

        for (Zone zone : zones) {
            if (!(zone instanceof DroneZone)) {
                newZones.add(zone);
            }
        }
        flightPlanCalculator.getCellGraphBuilder().setZones(newZones);
    }

}

package com.aeroflux.drone.domain.navigation.zoneService;

import java.util.ArrayList;
import java.util.List;

import com.aeroflux.drone.domain.model.RainCell;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.RainZone;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Zone;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.adapter.ZoneAdapter;

public class WeatherNavigationService {
    public static final double MAX_ALTITUDE = 120.0;
    public static final double MIN_ALTITUDE = 0.0;
    private final FlightPlanCalculator flightPlanCalculator;
    private final ZoneAdapter zoneAdapterFacade;

    public WeatherNavigationService(FlightPlanCalculator flightPlanCalculator, ZoneAdapter zoneAdapterFacade) {
        this.flightPlanCalculator = flightPlanCalculator;
        this.zoneAdapterFacade = zoneAdapterFacade;
    }

    public boolean add(final RainCell rainCell) {
        Zone weatherZone = null;

        try {
            weatherZone =  zoneAdapterFacade.adapt(rainCell);
        } catch (Exception e) {
            System.err.println("Error adapting RainCell: " + e.getMessage());
            return false;
        }

        return flightPlanCalculator.getCellGraphBuilder().getZones().add(weatherZone);
    }

    public boolean add(final List<RainCell> rainCells) {
        assert rainCells != null;

        boolean result = true;
        for (RainCell zone : rainCells) {
            result = result && add(zone);
        }

        return result;
    }

    public void clear() {
        List<Zone> newZones= new ArrayList<>();
        List<Zone> zones = flightPlanCalculator.getCellGraphBuilder().getZones();
        for (Zone zone : zones) {
            if (!(zone instanceof RainZone)) {
                newZones.add(zone);
            }
        }
        flightPlanCalculator.getCellGraphBuilder().setZones(newZones);
    }

    
}

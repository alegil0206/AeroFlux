package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculatorSingleton;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.RainZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class WeatherNavigationService {
    public static final double MAX_ALTITUDE = 120.0;
    public static final double MIN_ALTITUDE = 0.0;

    public WeatherNavigationService() {
       
    }
    public boolean add(final RainCell rainCell) {
        Zone weatherZone = null;

        try {
            weatherZone =  ZoneAdapterFacade.getInstance().adapt(rainCell);
        } catch (Exception e) {
            System.err.println("Error adapting RainCell: " + e.getMessage());
            return false;
        }

        return FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones().add(weatherZone);
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
        List<Zone> zones = FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().getZones();
        for (Zone zone : zones) {
            if (!(zone instanceof RainZone)) {
                newZones.add(zone);
            }
        }
        FlightPlanCalculatorSingleton.getInstance().getCellGraphBuilder().setZones(newZones);
    }

    
}

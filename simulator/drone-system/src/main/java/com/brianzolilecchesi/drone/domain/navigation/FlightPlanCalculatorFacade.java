package com.brianzolilecchesi.drone.domain.navigation;

import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.CellGraphBuilder;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.finder.BidirectionalDjikstraFinder;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.adapter.ZoneAdapter;
import com.brianzolilecchesi.drone.domain.navigation.zoneService.DroneZoneNavigationService;
import com.brianzolilecchesi.drone.domain.navigation.zoneService.GeozoneNavigationService;
import com.brianzolilecchesi.drone.domain.navigation.zoneService.WeatherNavigationService;

public class FlightPlanCalculatorFacade {

    private final GeozoneNavigationService geozoneService;
    private final DroneZoneNavigationService droneZoneService;
    private final WeatherNavigationService weatherService;
    private final ZoneAdapter zoneAdapter;
    private final FlightPlanCalculator flightPlanCalculator;

    public FlightPlanCalculatorFacade() {
        this.zoneAdapter = new ZoneAdapter();
        this.flightPlanCalculator = new FlightPlanCalculator(new BidirectionalDjikstraFinder(), new CellGraphBuilder() );
        this.geozoneService = new GeozoneNavigationService(flightPlanCalculator, zoneAdapter);
        this.droneZoneService = new DroneZoneNavigationService(flightPlanCalculator, zoneAdapter);
        this.weatherService = new WeatherNavigationService(flightPlanCalculator, zoneAdapter);
    }

    public GeozoneNavigationService getGeozoneService() {
        return geozoneService;
    }

    public DroneZoneNavigationService getDroneZoneService() {
        return droneZoneService;
    }

    public WeatherNavigationService getWeatherService() {
        return weatherService;
    }

    public ZoneAdapter getZoneAdapter() {
        return zoneAdapter;
    }

    public FlightPlanCalculator getFlightPlanCalculator() {
        return flightPlanCalculator;
    }
}
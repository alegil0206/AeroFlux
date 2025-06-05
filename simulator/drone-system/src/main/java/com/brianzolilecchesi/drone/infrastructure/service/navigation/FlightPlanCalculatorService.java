package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.CellGraphBuilder;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder.BidirectionalDjikstraFinder;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.DronePositionAdapter;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.GeozoneAdapter;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.GridZoneAdapter;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.WeatherZoneAdapter;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class FlightPlanCalculatorService {

    private final GeozoneNavigationService geozoneService;
    private final DroneZoneNavigationService droneZoneService;
    private final WeatherNavigationService weatherService;
    private final ZoneAdapterFacade zoneAdapterFacade;
    private final FlightPlanCalculator flightPlanCalculator;

    public FlightPlanCalculatorService() {
        this.zoneAdapterFacade = new ZoneAdapterFacade(new GridZoneAdapter(), new GeozoneAdapter(), new WeatherZoneAdapter(), new DronePositionAdapter());
        this.flightPlanCalculator = new FlightPlanCalculator(new BidirectionalDjikstraFinder(), new CellGraphBuilder() );
        this.geozoneService = new GeozoneNavigationService(flightPlanCalculator, zoneAdapterFacade);
        this.droneZoneService = new DroneZoneNavigationService(flightPlanCalculator, zoneAdapterFacade);
        this.weatherService = new WeatherNavigationService(flightPlanCalculator, zoneAdapterFacade);
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

    public ZoneAdapterFacade getZoneAdapterFacade() {
        return zoneAdapterFacade;
    }

    public FlightPlanCalculator getFlightPlanCalculator() {
        return flightPlanCalculator;
    }
}
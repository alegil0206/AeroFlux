package com.brianzolilecchesi.drone.domain.model;

import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.domain.service.navigation.DroneSafetyNavigationService;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.DroneZoneNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.GeozoneNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.WeatherNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;
import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.landing.LandingService;


public class DroneContext {
    public final DroneProperties props;
    public final HardwareAbstractionLayer hardwareAbstractionLayer;
    public final LogService logService;
    public final FlightController flightController;
    public final BatteryService batteryService;
    public final NavigationService navigationService;
    public final LandingService landingService;
    public final GeoZoneService geoZoneService;
    public final WeatherService weatherService;
    public final AuthorizationService authorizationService;
    public final SupportPointService supportPointService;
    public final GeozoneNavigationService geozoneNavService;
    public final WeatherNavigationService weatherNavService;
    public final DroneZoneNavigationService droneZoneNavService;
    public final CommunicationService communicationService;
    public final DroneSafetyNavigationService precedenceService;
    public DroneFlightMode flightMode = DroneFlightMode.NORMAL;
    public int stepCounter = 0;

    public DroneContext(DroneProperties props,
                        HardwareAbstractionLayer hal,
                        LogService log,
                        FlightController fc,
                        BatteryService batt,
                        NavigationService nav,
                        LandingService land,
                        GeoZoneService gzService,
                        WeatherService weatherService,
                        AuthorizationService authorizationService,
                        SupportPointService supportPointService,
                        GeozoneNavigationService gzNav,
                        WeatherNavigationService weatherNavService,
                        DroneZoneNavigationService droneZoneNavService,
                        CommunicationService comm,
                        DroneSafetyNavigationService prec) {
        this.props = props;
        this.hardwareAbstractionLayer = hal;
        this.logService = log;
        this.flightController = fc;
        this.batteryService = batt;
        this.navigationService = nav;
        this.landingService = land;
        this.geoZoneService = gzService;
        this.weatherService = weatherService;
        this.authorizationService = authorizationService; 
        this.supportPointService = supportPointService;
        this.geozoneNavService = gzNav;
        this.weatherNavService = weatherNavService;
        this.droneZoneNavService = droneZoneNavService;
        this.communicationService = comm;
        this.precedenceService = prec;
    }

    public int nextStep() {
        return stepCounter++;
    }

    public DroneFlightMode getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(DroneFlightMode flightMode) {
        this.flightMode = flightMode;
    }
}
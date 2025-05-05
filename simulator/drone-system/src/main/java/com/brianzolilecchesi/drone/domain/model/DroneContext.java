package com.brianzolilecchesi.drone.domain.model;

import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.domain.service.navigation.PrecedenceService;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.GeozoneNavigationService;
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
    public final GeozoneNavigationService geozoneNavService;
    public final CommunicationService communicationService;
    public final PrecedenceService precedenceService;
    public DroneFlightMode flightMode = DroneFlightMode.NORMAL;
    private int stepCounter = 0;

    public DroneContext(DroneProperties props,
                        HardwareAbstractionLayer hal,
                        LogService log,
                        FlightController fc,
                        BatteryService batt,
                        NavigationService nav,
                        LandingService land,
                        GeoZoneService gzService,
                        GeozoneNavigationService gzNav,
                        CommunicationService comm,
                        PrecedenceService prec) {
        this.props = props;
        this.hardwareAbstractionLayer = hal;
        this.logService = log;
        this.flightController = fc;
        this.batteryService = batt;
        this.navigationService = nav;
        this.landingService = land;
        this.geoZoneService = gzService;
        this.geozoneNavService = gzNav;
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
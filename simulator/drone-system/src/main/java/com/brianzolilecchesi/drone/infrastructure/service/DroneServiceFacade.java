package com.brianzolilecchesi.drone.infrastructure.service;

import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryService;
import com.brianzolilecchesi.drone.infrastructure.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.DroneSafetyNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;

public class DroneServiceFacade {

    private final LogService logService;
    private final BatteryService batteryService;
    private final CommunicationService communicationService;
    private final NavigationService navigationService;
    private final FlightController flightController;
    private final DroneSafetyNavigationService droneSafetyNavigationService;
    private final GeoZoneService geoZoneService;
    private final WeatherService weatherService;
    private final AuthorizationService authorizationService;
    private final SupportPointService supportPointService;

    
    public DroneServiceFacade(
            DroneContext context,
            HardwareAbstractionLayer hardwareAbstractionLayer
            ){
        logService = new LogService(context.getDroneProperties().getId());
        batteryService = new BatteryService(hardwareAbstractionLayer.getBattery(), logService);
        communicationService = new CommunicationService(hardwareAbstractionLayer.getRadio(), logService);
        navigationService = new NavigationService(logService, context.getSpeed());
        flightController = new FlightController(hardwareAbstractionLayer.getMotor(), hardwareAbstractionLayer.getGps(), hardwareAbstractionLayer.getAltimeter(), logService);
        droneSafetyNavigationService = new DroneSafetyNavigationService(context.getSpeed());
        geoZoneService = new GeoZoneService(logService);
        weatherService = new WeatherService(logService);
        authorizationService= new AuthorizationService(logService);
        supportPointService = new SupportPointService(logService);
    }

    public LogService getLogService() {
        return logService;
    }

    public BatteryService getBatteryService() {
        return batteryService;
    }

    public CommunicationService getCommunicationService() {
        return communicationService;
    }
    
    public NavigationService getNavigationService() {
        return navigationService;
    }

    public FlightController getFlightController() {
        return flightController;
    }

    public DroneSafetyNavigationService getDroneSafetyNavigationService() {
        return droneSafetyNavigationService;
    }

    public GeoZoneService getGeoZoneService() {
        return geoZoneService;
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public SupportPointService getSupportPointService() {
        return supportPointService;
    }

}

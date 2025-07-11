package com.aeroflux.drone.infrastructure.service;

import java.util.Map;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.aeroflux.drone.domain.integration.GeoAuthorizationGateway;
import com.aeroflux.drone.domain.integration.GeoAwarenessGateway;
import com.aeroflux.drone.domain.integration.WeatherGateway;
import com.aeroflux.drone.domain.model.DroneContext;
import com.aeroflux.drone.infrastructure.component.HardwareAbstractionLayer;
import com.aeroflux.drone.infrastructure.controller.FlightController;
import com.aeroflux.drone.infrastructure.integration.GeoAuthorizationRestClient;
import com.aeroflux.drone.infrastructure.integration.GeoAwarenessRestClient;
import com.aeroflux.drone.infrastructure.integration.WeatherServiceRestClient;
import com.aeroflux.drone.infrastructure.service.authorization.AuthorizationService;
import com.aeroflux.drone.infrastructure.service.battery.BatteryService;
import com.aeroflux.drone.infrastructure.service.communication.CommunicationService;
import com.aeroflux.drone.infrastructure.service.geozone.GeoZoneService;
import com.aeroflux.drone.infrastructure.service.log.LogService;
import com.aeroflux.drone.infrastructure.service.navigation.DroneSafetyNavigationService;
import com.aeroflux.drone.infrastructure.service.navigation.NavigationService;
import com.aeroflux.drone.infrastructure.service.supportPoint.SupportPointService;
import com.aeroflux.drone.infrastructure.service.weather.WeatherService;

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
            HardwareAbstractionLayer hardwareAbstractionLayer,
            Map<String, String> microservicesUrlsMap
            ){
        logService = new LogService(context.getDroneProperties().getId());
        batteryService = new BatteryService(hardwareAbstractionLayer.getBattery(), logService);
        communicationService = new CommunicationService(hardwareAbstractionLayer.getRadio(), logService);
        navigationService = new NavigationService(logService);
        flightController = new FlightController(hardwareAbstractionLayer.getMotor(), hardwareAbstractionLayer.getGps(), hardwareAbstractionLayer.getAltimeter(), logService);
        droneSafetyNavigationService = new DroneSafetyNavigationService();

        WeatherGateway weatherServiceRestClient = new WeatherServiceRestClient(microservicesUrlsMap.get("WEATHER"), createRestTemplateWithTimeout());
        GeoAuthorizationGateway geoAuthorizationRestClient = new GeoAuthorizationRestClient(microservicesUrlsMap.get("GEO_AUTHORIZATION"), createRestTemplateWithTimeout());
        GeoAwarenessGateway geoAwarenessRestClient = new GeoAwarenessRestClient(microservicesUrlsMap.get("GEO_AWARENESS"), createRestTemplateWithTimeout());
        geoZoneService = new GeoZoneService(logService, geoAwarenessRestClient);
        weatherService = new WeatherService(logService, weatherServiceRestClient);
        authorizationService= new AuthorizationService(logService, geoAuthorizationRestClient);
        supportPointService = new SupportPointService(logService, geoAwarenessRestClient);
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

    private static RestTemplate createRestTemplateWithTimeout() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return new RestTemplate(factory);
    }
}

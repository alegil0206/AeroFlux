package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationDTO;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessService;
import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationService;
import com.brianzolilecchesi.drone.domain.integration.WeatherService;

import java.util.List;

public class RestApiGateway {

    private final WeatherService weatherService;
    private final GeoAwarenessService geoAwarenessService;
    private final GeoAuthorizationService geoAuthorizationService;

    public RestApiGateway(WeatherService weatherService,
                          GeoAwarenessService geoAwarenessService,
                          GeoAuthorizationService geoAuthorizationService) {
        this.weatherService = weatherService;
        this.geoAwarenessService = geoAwarenessService;
        this.geoAuthorizationService = geoAuthorizationService;
    }

    public List<RainCellDTO> getWeatherData() {
        return weatherService.getWeather();
    }

    public List<GeoZoneDTO> getGeoZones() {
        return geoAwarenessService.getGeoZones();
    }

    public List<AuthorizationDTO> getAuthorizations(String droneId) {
        return geoAuthorizationService.getAuthorizations(droneId);
    }

    public AuthorizationDTO requestAuthorization(AuthorizationDTO authorizationDTO) {
        return geoAuthorizationService.requestAuthorization(authorizationDTO);
    }
}

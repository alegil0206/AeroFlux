package com.brianzolilecchesi.drone.infrastructure.integration;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessClient;
import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationClient;
import com.brianzolilecchesi.drone.domain.integration.WeatherClient;

import java.util.List;

public class RestApiGateway {

    private final WeatherClient weatherService;
    private final GeoAwarenessClient geoAwarenessService;
    private final GeoAuthorizationClient geoAuthorizationService;

    public RestApiGateway(WeatherClient weatherService,
                          GeoAwarenessClient geoAwarenessService,
                          GeoAuthorizationClient geoAuthorizationService) {
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

    public List<AuthorizationResponseDTO> getAuthorizations(String droneId) {
        return geoAuthorizationService.getAuthorizations(droneId);
    }

    public AuthorizationResponseDTO requestAuthorization(AuthorizationRequestDTO authorizationDTO) throws AuthorizationException {
        return geoAuthorizationService.requestAuthorization(authorizationDTO);
    }
}

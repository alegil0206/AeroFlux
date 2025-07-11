package com.aeroflux.drone.infrastructure.integration;

import com.aeroflux.drone.domain.dto.RainCellDTO;
import com.aeroflux.drone.domain.exception.ExternalServiceException;
import com.aeroflux.drone.domain.integration.WeatherGateway;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class WeatherServiceRestClient implements WeatherGateway {
    private final RestTemplate restTemplate;
    private final String weatherApiUrl;

    public WeatherServiceRestClient(String weatherApiUrl, RestTemplate restTemplate) {
        this.weatherApiUrl = weatherApiUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RainCellDTO> getWeather() throws ExternalServiceException {
        try {
            ResponseEntity<RainCellDTO[]> response = restTemplate.exchange(
                weatherApiUrl + "/weather/rain-cell",
                HttpMethod.GET,
                null,
                RainCellDTO[].class
            );
            return response.getBody() != null ? List.of(response.getBody()) : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error fetching weather data: " + e.getMessage());
        }
    }
}


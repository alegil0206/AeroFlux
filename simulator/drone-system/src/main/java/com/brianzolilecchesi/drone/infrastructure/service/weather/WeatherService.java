package com.brianzolilecchesi.drone.infrastructure.service.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.model.WeatherData;

import com.brianzolilecchesi.drone.infrastructure.integration.RestApiGateway;

public class WeatherService {

    private final RestApiGateway restApiGateway;

    public WeatherService(RestApiGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
    }

    
    public List<WeatherData> getWeatherData() {
        try {
            List<RainCellDTO> rainCellDTOList = restApiGateway.getWeatherData();
            List<WeatherData> weatherDataList = new ArrayList<>();
            
            for (RainCellDTO rainCellDTO : rainCellDTOList) {
                weatherDataList.add(new WeatherData(rainCellDTO.getCoordinates()));
            }
            
            return weatherDataList;
            
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: weather data not found - " + e.getMessage());
            return Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("Error in communication with the weather service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}

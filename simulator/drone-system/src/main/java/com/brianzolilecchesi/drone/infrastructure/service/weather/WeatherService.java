package com.brianzolilecchesi.drone.infrastructure.service.weather;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.integration.WeatherGateway;
import com.brianzolilecchesi.drone.domain.model.RainCell;

public class WeatherService {

    private final WeatherGateway restApiGateway;
    private List<RainCell> rainCells;

    public WeatherService(WeatherGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
        this.rainCells = new ArrayList<>();
    }

    public void fetchRainCells() {
        try {
            List<RainCellDTO> rainCellDTOList = restApiGateway.getWeather();
            rainCells = rainCellDTOList.stream()
                    .map(rainCellDTO -> new RainCell(rainCellDTO.getCoordinates()))
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: weather data not found - " + e.getMessage());
        } catch (RestClientException e) {
            System.err.println("Error in communication with the weather service: " + e.getMessage());
        }
    }

    public List<RainCell> getRainCells() {
        return rainCells;
    }
}

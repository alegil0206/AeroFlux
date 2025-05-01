package com.brianzolilecchesi.drone.infrastructure.service.weather;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.WeatherGateway;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.integration.WeatherServiceRestClient;

public class WeatherService {

    private final WeatherGateway restApiGateway;
    private List<RainCell> rainCells;

    public WeatherService() {
        this.restApiGateway = new WeatherServiceRestClient();
        this.rainCells = new ArrayList<>();
    }

    public void fetchRainCells() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getWeather();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(
                rainCellDTOs -> {
                    if (rainCellDTOs != null) {
                        rainCells = rainCellDTOs.stream()
                                .map(rainCellDTO -> new RainCell(rainCellDTO.getCoordinates()))
                                .toList();
                    }
                })
                .exceptionally(e -> {
                    Throwable cause = e.getCause();
                    if (cause instanceof ExternalServiceException) {
                        System.err.println("Error communicating with weather service: " + cause.getMessage());
                    } else {
                        System.err.println("Unexpected error: " + e.getMessage());
                    }
                    return null;
                });
    }

    public List<RainCell> getRainCells() {
        return rainCells;
    }
}

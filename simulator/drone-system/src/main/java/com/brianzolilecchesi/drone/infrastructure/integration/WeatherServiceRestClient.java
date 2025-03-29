package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.integration.WeatherService;

import java.util.ArrayList;
import java.util.List;


public class WeatherServiceRestClient implements WeatherService {
    @Override
    public List<RainCellDTO> getWeather() {
        // Implementazione che utilizza un client HTTP per recuperare i dati meteo
        System.out.println("Chiamata all'API meteo");
        return new ArrayList<>();
    }
}

package com.brianzolilecchesi.weather.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.weather.service.WeatherService;
import com.brianzolilecchesi.weather.dto.WeatherCellDTO;

@RestController
@RequestMapping(WeatherController.WEATHER_BASE_URL)
public class WeatherController {
	
	public static final String WEATHER_BASE_URL = "/weather";

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/cells")
    public List<WeatherCellDTO> getWeatherGrid() {
        return weatherService.getWeatherGrid();
    }
}


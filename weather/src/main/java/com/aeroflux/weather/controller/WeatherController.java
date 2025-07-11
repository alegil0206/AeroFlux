package com.aeroflux.weather.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.weather.service.WeatherService;
import com.aeroflux.weather.dto.RainCellDTO;
import com.aeroflux.weather.dto.WeatherConfigDTO;

@RestController
@RequestMapping(WeatherController.WEATHER_BASE_URL)
public class WeatherController {
	
	public static final String WEATHER_BASE_URL = "/weather";

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/rain-cell")
    public List<RainCellDTO> getRainPolygons() {
        return weatherService.getRainCells();
    }

    @GetMapping("/config")
    public WeatherConfigDTO getConfig() {
        return weatherService.getConfig();
    }

    @PutMapping("/config")
    public WeatherConfigDTO updateConfig(@RequestBody WeatherConfigDTO config) {
        return weatherService.updateConfig(config);
    }
}


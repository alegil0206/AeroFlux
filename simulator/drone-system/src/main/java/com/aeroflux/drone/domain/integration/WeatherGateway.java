package com.aeroflux.drone.domain.integration;

import com.aeroflux.drone.domain.dto.RainCellDTO;
import com.aeroflux.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface WeatherGateway {
    
    List<RainCellDTO> getWeather() throws ExternalServiceException;
}

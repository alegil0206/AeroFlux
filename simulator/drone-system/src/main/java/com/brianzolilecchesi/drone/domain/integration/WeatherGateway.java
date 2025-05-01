package com.brianzolilecchesi.drone.domain.integration;

import com.brianzolilecchesi.drone.domain.dto.RainCellDTO;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface WeatherGateway {
    
    List<RainCellDTO> getWeather() throws ExternalServiceException;
}

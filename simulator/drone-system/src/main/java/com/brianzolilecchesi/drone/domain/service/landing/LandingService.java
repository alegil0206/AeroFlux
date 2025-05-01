package com.brianzolilecchesi.drone.domain.service.landing;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public interface LandingService {
    Boolean evaluateLandingZone(Coordinate position);
}
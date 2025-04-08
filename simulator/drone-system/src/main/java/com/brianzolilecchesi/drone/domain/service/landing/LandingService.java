package com.brianzolilecchesi.drone.domain.service.landing;

import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.LandingZone;

public interface LandingService {
    LandingZone evaluateLandingZone(Coordinate position);
}
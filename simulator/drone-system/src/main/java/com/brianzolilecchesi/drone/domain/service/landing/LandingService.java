package com.brianzolilecchesi.drone.domain.service.landing;

import com.brianzolilecchesi.drone.domain.model.Coordinates;
import com.brianzolilecchesi.drone.domain.model.LandingZone;

public interface LandingService {
    LandingZone evaluateLandingZone(Coordinates position);
}
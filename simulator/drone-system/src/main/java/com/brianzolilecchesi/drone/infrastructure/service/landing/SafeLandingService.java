package com.brianzolilecchesi.drone.infrastructure.service.landing;

import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.LandingZone;
import com.brianzolilecchesi.drone.domain.component.Camera;


public class SafeLandingService implements LandingService{

    Camera camera;
    LogService logService;

    public SafeLandingService(Camera camera, LogService logService) {
        this.camera = camera;
        this.logService = logService;
    }

    @Override
    public LandingZone evaluateLandingZone(Coordinate coordinates) {
        String image = camera.takePicture(coordinates);
        if (image.contains("water")) {
            return new LandingZone(false);
        }
        return new LandingZone(true);
    }
    
}

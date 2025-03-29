package com.brianzolilecchesi.drone.infrastructure.service.landing;

import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
import com.brianzolilecchesi.drone.domain.model.Coordinates;
import com.brianzolilecchesi.drone.domain.model.LandingZone;
import com.brianzolilecchesi.drone.domain.component.Camera;


public class SafeLandingService implements LandingService{

    Camera camera;

    public SafeLandingService(Camera camera) {
        this.camera = camera;
    }

    @Override
    public LandingZone evaluateLandingZone(Coordinates coordinates) {
        String image = camera.takePicture(coordinates);
        if (image.contains("water")) {
            return new LandingZone(false);
        }
        return new LandingZone(true);
    }
    
}

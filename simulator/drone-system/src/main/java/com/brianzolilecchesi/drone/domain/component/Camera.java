package com.brianzolilecchesi.drone.domain.component;

import com.brianzolilecchesi.drone.domain.model.Coordinates;

public interface Camera {
    String takePicture(Coordinates coordinates);
}
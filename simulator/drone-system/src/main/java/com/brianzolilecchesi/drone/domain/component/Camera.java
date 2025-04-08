package com.brianzolilecchesi.drone.domain.component;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public interface Camera {
    String takePicture(Coordinate coordinates);
}
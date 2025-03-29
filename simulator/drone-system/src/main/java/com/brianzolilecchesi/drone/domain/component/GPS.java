package com.brianzolilecchesi.drone.domain.component;

import com.brianzolilecchesi.drone.domain.model.Coordinates;

public interface GPS {
    double getLatitude();
    double getLongitude();
    Coordinates getCoordinates();
}

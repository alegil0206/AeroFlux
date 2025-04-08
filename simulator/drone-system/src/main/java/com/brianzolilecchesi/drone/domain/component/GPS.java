package com.brianzolilecchesi.drone.domain.component;

import com.brianzolilecchesi.drone.domain.model.Coordinate;

public interface GPS {
    double getLatitude();
    double getLongitude();
    Coordinate getCoordinates();
}

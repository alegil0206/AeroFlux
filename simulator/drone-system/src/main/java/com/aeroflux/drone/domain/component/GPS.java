package com.aeroflux.drone.domain.component;

import com.aeroflux.drone.domain.model.Coordinate;

public interface GPS {
    double getLatitude();
    double getLongitude();
    Coordinate getCoordinates();
}

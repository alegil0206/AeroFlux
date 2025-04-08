package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.geo;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;

public enum GeoCalculatorSingleton {
    INSTANCE;
    
    private final GeoCalculator calculator = new GeoCalculator(Ellipsoid.WGS84, new GeodeticCalculator());

    public GeoCalculator getInstance() {
        return calculator;
    }
}
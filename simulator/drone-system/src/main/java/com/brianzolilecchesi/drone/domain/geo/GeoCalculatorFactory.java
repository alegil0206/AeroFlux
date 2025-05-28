package com.brianzolilecchesi.drone.domain.geo;

public class GeoCalculatorFactory {

    private static final GeoShapeCalculator SHAPE_CALCULATOR = new GeoShapeCalculator();
    private static final GeoDistanceCalculator DISTANCE_CALCULATOR = new GeoDistanceCalculator();

    private GeoCalculatorFactory() {
    }

    public static GeoShapeCalculator getGeoShapeCalculator() {
        return SHAPE_CALCULATOR;
    }

    public static GeoDistanceCalculator getGeoDistanceCalculator() {
        return DISTANCE_CALCULATOR;
    }
}
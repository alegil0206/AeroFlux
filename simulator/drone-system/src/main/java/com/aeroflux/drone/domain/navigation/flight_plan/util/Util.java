package com.aeroflux.drone.domain.navigation.flight_plan.util;

public class Util {

	public static final double EARTH_RADIUS = 6378137.0;

	public static double min(double... values) {
		double min = Double.MAX_VALUE;
		for (double value : values) {
			min = Math.min(min, value);
		}
		return min;
	}

	public static double max(double... values) {
		double max = -Double.MAX_VALUE;
		for (double value : values) {
			max = Math.max(max, value);
		}
		return max;
	}

	

}
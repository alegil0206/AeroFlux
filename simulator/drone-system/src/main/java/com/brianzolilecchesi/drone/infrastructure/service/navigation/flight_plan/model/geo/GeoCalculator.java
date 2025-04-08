package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.geo;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.gavaghan.geodesy.GlobalCoordinates;

import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.Position;

public class GeoCalculator {
	
	private final Ellipsoid ellipsoid;
	private final GeodeticCalculator calculator;
	
	GeoCalculator(Ellipsoid ellipsoid, GeodeticCalculator calculator) {
		this.ellipsoid = ellipsoid;
		this.calculator = calculator;
	}
	
	public double distance(Coordinate c1, Coordinate c2) {
		assert c1 != null;
		assert c2 != null;
		
		GlobalPosition p1 = new GlobalPosition(c1.getLatitude(), c1.getLongitude(), 0);
		GlobalPosition p2 = new GlobalPosition(c2.getLatitude(), c2.getLongitude(), 0);
		
		return calculator.calculateGeodeticCurve(ellipsoid, p1, p2).getEllipsoidalDistance();
	}
	
	public double distance(Position p1, Position p2, boolean includeAltitude) {
		assert p1 != null;
		assert p2 != null;
		
		GlobalPosition gp1 = new GlobalPosition(p1.getLatitude(), p1.getLongitude(), 0);
		GlobalPosition gp2 = new GlobalPosition(p2.getLatitude(), p2.getLongitude(), 0);
		
		double distance = calculator.calculateGeodeticCurve(ellipsoid, gp1, gp2).getEllipsoidalDistance();
		if (includeAltitude) {
			distance = Math.sqrt(
					Math.pow(distance, 2) + 
					Math.pow(p1.getAltitude() - p2.getAltitude(), 2)
					);
		}
		
		return distance;
	}
	
	public double distance(Position c1, Position c2) {
		return distance(c1, c2, true);
	}
	
	public double bearing(Coordinate p1, Coordinate p2) {
		assert p1 != null;
		assert p2 != null;
		
		GlobalPosition gp1 = new GlobalPosition(p1.getLatitude(), p1.getLongitude(), 0);
		GlobalPosition gp2 = new GlobalPosition(p2.getLatitude(), p2.getLongitude(), 0);

		return calculator.calculateGeodeticCurve(ellipsoid, gp1, gp2).getAzimuth();
	}
	
	public Coordinate moveByBearing(
			final Coordinate c, 
			double azimuth, 
			double distance 
			) {
		assert c != null;
		assert azimuth >= 0 && azimuth <= 360;
		assert distance >= 0;
		
		GlobalCoordinates gc = new GlobalCoordinates(c.getLatitude(), c.getLongitude());
		GlobalCoordinates moved = calculator.calculateEndingGlobalCoordinates(ellipsoid, gc, azimuth, distance);
		
		return new Coordinate(moved.getLatitude(), moved.getLongitude());
	}
	
	public Coordinate moveByDisplacement(
			final Coordinate c, 
			double deltaLat, 
			double deltaLon
			) {
		assert c != null;
		
        double latAzimuth = deltaLat >= 0 ? 0 : 180;
        Coordinate latMoved = moveByBearing(c, latAzimuth, Math.abs(deltaLat));

        double lonAzimuth = deltaLon >= 0 ? 90 : 270;
		return moveByBearing(latMoved, lonAzimuth, Math.abs(deltaLon));
	}
	
	public Position moveByBearing(
			final Position position, 
			double azimuth, 
			double distance,
			double deltaAlt
			) {
		assert position != null;
		assert azimuth >= 0 && azimuth <= 360;
		
		return new Position(
				moveByBearing(position, azimuth, distance),
				position.getAltitude() + deltaAlt
				);
	}
		
	public Position moveByDisplacement(
			final Position position, 
			double deltaLat, 
			double deltaLon, 
			double deltaAlt
			) {
		assert position != null;
		
        double latAzimuth = deltaLat >= 0 ? 0 : 180;
        Coordinate latMoved = moveByBearing(position, latAzimuth, Math.abs(deltaLat));

        double lonAzimuth = deltaLon >= 0 ? 90 : 270;
        Coordinate lonMoved = moveByBearing(latMoved, lonAzimuth, Math.abs(deltaLon));
		
		return new Position(
				lonMoved,
				position.getAltitude() + deltaAlt
				);
	}
}
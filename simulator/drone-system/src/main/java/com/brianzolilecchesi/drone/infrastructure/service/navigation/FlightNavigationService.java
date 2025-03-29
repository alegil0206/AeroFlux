package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.model.Position;
import java.util.List;
import java.util.ArrayList;

import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;

public class FlightNavigationService implements NavigationService {

    private final GPS gps;
    private final Altimeter altimeter;
    private List<Position> flightPlan = new ArrayList<>();
    private int currentPositionIndex = 0;
    private static final double EARTH_RADIUS = 6378137.0; 

    public FlightNavigationService(GPS gps, Altimeter altimeter) {
        this.gps = gps;
        this.altimeter = altimeter;
    }

    @Override
    public Position getCurrentPosition() {
        return new Position(gps.getLatitude(), gps.getLongitude(), altimeter.getAltitude());
    }

    @Override
    public void calculateFlightPlan(Position start, Position destination) {
        flightPlan.clear();
        flightPlan.add(start);

        double currentLatitude = start.getLatitude();
        double currentLongitude = start.getLongitude();
        double destinationLatitude = destination.getLatitude();
        double destinationLongitude = destination.getLongitude();
        double bearing = Math.atan2(destinationLongitude - currentLongitude, destinationLatitude - currentLatitude);

        while (calculateDistance(currentLatitude, currentLongitude, destinationLatitude, destinationLongitude) > 1000) {
            double distance = 1000; // 1 km step
            double newLatitude = Math.asin(Math.sin(Math.toRadians(currentLatitude)) * Math.cos(distance / EARTH_RADIUS)
                    + Math.cos(Math.toRadians(currentLatitude)) * Math.sin(distance / EARTH_RADIUS)
                            * Math.cos(bearing));
            double newLongitude = Math.toRadians(currentLongitude) + Math.atan2(
                    Math.sin(bearing) * Math.sin(distance / EARTH_RADIUS) * Math.cos(Math.toRadians(currentLatitude)),
                    Math.cos(distance / EARTH_RADIUS)
                            - Math.sin(Math.toRadians(currentLatitude)) * Math.sin(newLatitude));
            newLongitude = Math.toDegrees(newLongitude);
            newLatitude = Math.toDegrees(newLatitude);

            double newAltitude = 40.0;

            start = new Position(newLatitude, newLongitude, newAltitude);
            currentLatitude = newLatitude;
            currentLongitude = newLongitude;

            flightPlan.add(start);
        }


        flightPlan.add(destination);
        currentPositionIndex = 0;
    }

    @Override
    public Position followFlightPlan() {
        if (currentPositionIndex < flightPlan.size()) {
            Position nextPosition = flightPlan.get(currentPositionIndex);
            currentPositionIndex++;
            return nextPosition;
        } else {
            return null; // No more positions in the flight plan
        }
    }

    @Override
    public boolean hasReached(Position position) {
        Position currentPosition = getCurrentPosition();
        return currentPosition.equals(position);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}

package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public class DroneSafetyNavigationService {

    private double collisionAvoidanceThreshold;
    private double verticalCollisionAvoidanceThreshold = 20.0;
    private double selfSeparationThreshold;
    
    public DroneSafetyNavigationService(double droneMaxSpeed) {
        collisionAvoidanceThreshold = droneMaxSpeed;
        selfSeparationThreshold = 160.0 + 2 * droneMaxSpeed;
    }

    public boolean hasPriority(NearbyDroneStatus droneStatus, NearbyDroneStatus otherDroneStatus) {
            if (droneStatus.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING && otherDroneStatus.getFlightMode() != DroneFlightMode.EMERGENCY_LANDING) return true;
            if (droneStatus.getFlightMode() != DroneFlightMode.EMERGENCY_LANDING && otherDroneStatus.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) return false;
        
            if (droneStatus.getOperationCategory().equals(DroneProperties.DRONE_CERTIFIED_OPERATION_CATEGORY) && !otherDroneStatus.getOperationCategory().equals(DroneProperties.DRONE_CERTIFIED_OPERATION_CATEGORY)) return true;
            if (!droneStatus.getOperationCategory().equals(DroneProperties.DRONE_CERTIFIED_OPERATION_CATEGORY) && otherDroneStatus.getOperationCategory().equals(DroneProperties.DRONE_CERTIFIED_OPERATION_CATEGORY)) return false;

            if (droneStatus.getPosition().getAltitude() < otherDroneStatus.getPosition().getAltitude()) return true;
            if (droneStatus.getPosition().getAltitude() > otherDroneStatus.getPosition().getAltitude()) return false;
        
            return droneStatus.getDroneId().compareTo(otherDroneStatus.getDroneId()) < 0;
    }
    
    public boolean conflictCondition(List<Position> nextA, List<Position> nextB) {
        int maxSteps = Math.max(nextA.size(), nextB.size());

        for (int i = 0; i < maxSteps; i++) {    
            Position posA = i < nextA.size() ? nextA.get(i) : nextA.get(nextA.size() - 1);
            for (int j = Math.max(0, i - 2); j <= Math.min(nextB.size() - 1, i + 2); j++) {
                Position posB = nextB.get(j);
                if (posA.distance(posB) <= collisionAvoidanceThreshold && Math.abs(posA.getAltitude() - posB.getAltitude()) <= verticalCollisionAvoidanceThreshold) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean headToHeadCondition(List<Position> nextA, List<Position> nextB) {
        Position a1 = nextA.getFirst();
        Position a2 = nextA.getLast();
        Position b1 = nextB.getFirst();
        Position b2 = nextB.getLast();

        double[] vA = { a2.getLatitude() - a1.getLatitude(), a2.getLongitude() - a1.getLongitude() };
        double[] vB = { b2.getLatitude() - b1.getLatitude(), b2.getLongitude() - b1.getLongitude() };

        double dot = vA[0]*vB[0] + vA[1]*vB[1];
        double magA = Math.sqrt(vA[0]*vA[0] + vA[1]*vA[1]);
        double magB = Math.sqrt(vB[0]*vB[0] + vB[1]*vB[1]);

        if (magA == 0 || magB == 0) return false;

        double cosTheta = dot / (magA * magB);
        double angle = Math.acos(cosTheta) * 180 / Math.PI;

        boolean angleOpposite = angle > 135;

        double[] rAB = { b1.getLatitude() - a1.getLatitude(), b1.getLongitude() - a1.getLongitude() };
        double dotA = vA[0]*rAB[0] + vA[1]*rAB[1];
        double dotB = vB[0]*(-rAB[0]) + vB[1]*(-rAB[1]);

        return angleOpposite && dotA > 0 && dotB > 0;
    }

    public boolean isInConflictVolume(Position dronePosition, Position otherDronePosition) {
        return dronePosition.distance(otherDronePosition) < collisionAvoidanceThreshold;
    }

    public boolean isInSelfSeparationVolume(Position dronePosition, Position otherDronePosition) {
        return dronePosition.distance(otherDronePosition) < selfSeparationThreshold;
    }

}

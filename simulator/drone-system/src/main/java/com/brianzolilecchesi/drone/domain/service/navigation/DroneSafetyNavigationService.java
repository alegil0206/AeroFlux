package com.brianzolilecchesi.drone.domain.service.navigation;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public interface DroneSafetyNavigationService {

    public boolean hasPriority(NearbyDroneStatus droneStatus, NearbyDroneStatus otherDroneStatus);
    public boolean conflictCondition(List<Position> nextA, List<Position> nextB);
    public boolean headToHeadCondition(List<Position> nextA, List<Position> nextB);
    public boolean isInConflictVolume(Position dronePosition, Position otherDronePosition);
    public boolean isInSelfSeparationVolume(Position dronePosition, Position otherDronePosition);
}

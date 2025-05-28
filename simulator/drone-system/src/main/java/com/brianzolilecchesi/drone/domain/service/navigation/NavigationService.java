package com.brianzolilecchesi.drone.domain.service.navigation;

import java.util.List;

import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public interface NavigationService {
	
    void generateFlightPlan(Position start, Position destination);
    void optimizeFlightPlan();
    Position followFlightPlan();
    boolean hasReached(Position actualPosition, Position destination);
    boolean hasReached(Coordinate actualCoordinate, Coordinate destination);
    FlightPlanDTO getFlightPlan();
    void configureVerticalLanding(Position position);
    List<Position> getNextWaypoints();
    DataStatus getFlightPlanStatus();
    void adaptFlightPlan();
}
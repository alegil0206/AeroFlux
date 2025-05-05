package com.brianzolilecchesi.drone.domain.service.navigation;

import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public interface NavigationService {
	
    void generateFlightPlan(Position start, Position destination);
    void optimizeFlightPlan();
    Position followFlightPlan();
    boolean hasReached(Position position);
    boolean hasReached(Coordinate coordinate);
    FlightPlanDTO getFlightPlan();
    void configureVerticalLanding(Position position);
    Position getNextPosition();
    DataStatus getFlightPlanStatus();
}
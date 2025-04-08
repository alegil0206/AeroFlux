package com.brianzolilecchesi.drone.domain.service.navigation;

import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Position;

public interface NavigationService {
	
    void calculateFlightPlan(Position start, Position destination);
    Position followFlightPlan();
    boolean hasReached(Position position);
    Position getCurrentPosition();
    FlightPlanDTO getFlightPlan();
}
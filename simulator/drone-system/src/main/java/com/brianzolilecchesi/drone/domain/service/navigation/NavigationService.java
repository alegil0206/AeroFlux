package com.brianzolilecchesi.drone.domain.service.navigation;

import javax.xml.crypto.Data;

import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.Position;

public interface NavigationService {
	
    void calculateFlightPlan(Position start, Position destination);
    Position followFlightPlan();
    boolean hasReached(Position position);
    boolean hasReached(Coordinate coordinate);
    Position getCurrentPosition();
    FlightPlanDTO getFlightPlan();
    boolean isOnGround();
    void configureVerticalLanding();
    Position getNextPosition();
    DataStatus getFlightPlanStatus();
}
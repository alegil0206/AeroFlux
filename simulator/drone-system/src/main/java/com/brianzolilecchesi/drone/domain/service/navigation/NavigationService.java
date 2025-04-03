package com.brianzolilecchesi.drone.domain.service.navigation;

import com.brianzolilecchesi.drone.domain.model.Position;
import java.util.List;

public interface NavigationService {
    void calculateFlightPlan(Position start, Position destination);
    Position followFlightPlan();
    boolean hasReached(Position position);
    Position getCurrentPosition();
    List<Position> getFlightPlan();
}
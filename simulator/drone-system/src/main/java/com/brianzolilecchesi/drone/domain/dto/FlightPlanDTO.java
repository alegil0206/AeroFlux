package com.brianzolilecchesi.drone.domain.dto;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.FlightPlan;


public class FlightPlanDTO {
    
    private List<Position> positions;

    public FlightPlanDTO() {
    }

    public FlightPlanDTO(List<Position> positions) {
        this.positions = positions;
    }

    public FlightPlanDTO(FlightPlan flightPlan){
        this.positions = flightPlan.getPathPositions();
    }

    public List<Position> getPositions() {
        return positions;
    }
}

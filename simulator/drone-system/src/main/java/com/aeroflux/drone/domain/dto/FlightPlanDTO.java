package com.aeroflux.drone.domain.dto;

import java.util.List;

import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.navigation.flight_plan.model.FlightPlan;


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

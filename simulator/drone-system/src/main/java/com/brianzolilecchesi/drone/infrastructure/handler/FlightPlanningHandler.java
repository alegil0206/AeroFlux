package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.List;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.Position;

public class FlightPlanningHandler implements StepHandler {

    @Override
    public boolean handle(DroneContext ctx) {
        DataStatus status = ctx.navigationService.getFlightPlanStatus();
        if (status == DataStatus.AVAILABLE) return false;
        if (status == DataStatus.LOADING) 
            if (ctx.flightController.isOnGround())
                return true; 
            else 
                return false;

        DataStatus gzStatus = ctx.geoZoneService.getGeoZonesStatus();
        if (gzStatus == DataStatus.AVAILABLE) {
            List<GeoZone> zones = ctx.geoZoneService.getGeoZones();
            ctx.geozoneNavService.addGeoZones(zones);
            ctx.navigationService.generateFlightPlan(
                new Position(ctx.props.getSource(), 0),
                new Position(ctx.props.getDestination(), 0)
            );
        }
        return true;
    }
    
}



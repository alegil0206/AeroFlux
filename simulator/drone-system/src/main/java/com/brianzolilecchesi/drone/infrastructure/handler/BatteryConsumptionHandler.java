package com.brianzolilecchesi.drone.infrastructure.handler;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.SupportPoint;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;

public class BatteryConsumptionHandler implements StepHandler {

    private final DroneContext context;
    private final BatteryService batteryService;
    private final LogService logService;
    private final FlightController flightController;
    private final NavigationService navigationService;
    private final SupportPointService supportPointService;

    public BatteryConsumptionHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.batteryService = droneServices.getBatteryService();
        this.logService = droneServices.getLogService();
        this.flightController = droneServices.getFlightController();
        this.navigationService = droneServices.getNavigationService();
        this.supportPointService = droneServices.getSupportPointService();
    }

    @Override
    public boolean handle() {
        
        Position currentPosition = flightController.getCurrentPosition();

        if(batteryService.isBatteryCritical()){
            if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) return false;
            logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CRITICAL, "Battery critical, emergency landing initiated");
            context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING);
            navigationService.configureVerticalLanding(currentPosition);
            context.setCurrentDestination(new Position(currentPosition, 0));
            return false;
        }
        
        if (navigationService.getFlightPlanStatus() != DataStatus.AVAILABLE) return false;
        double flightDistance = navigationService.getFlightDistanceToEnd();

        if(batteryService.isBatteryEnoughForFlight(flightDistance)) return false;

        DataStatus supportPointStatus = supportPointService.getSupportPointsStatus();

        if(supportPointStatus != DataStatus.AVAILABLE) {
            if (supportPointStatus == DataStatus.NOT_REQUESTED ||
                supportPointStatus == DataStatus.FAILED ) {            
                    supportPointService.fetchSupportPoints();
            }
            if (!flightController.isOnGround())
                flightController.hover();
            return true;
        }
        
        List<SupportPoint> supportPoints = new ArrayList<>();
        supportPoints.add(new SupportPoint("Destination Point", "Destination Point", context.getDroneProperties().getDestination()));
        supportPoints.add(new SupportPoint("Source Point", "Source Point", context.getDroneProperties().getSource()));
        supportPoints.addAll(supportPointService.getSupportPoints());

        SupportPoint closestSupportPoint = supportPointService.getClosestSupportPoint(currentPosition, supportPoints);
        Position closestSupportPointPosition = new Position(closestSupportPoint.getCoordinate(), 0);
        if(context.getCurrentDestination().equals(closestSupportPointPosition)) return false;
        
        context.setCurrentDestination(closestSupportPointPosition);

        logService.info(
            LogConstants.Component.BATTERY_HANDLER,
            LogConstants.Event.INSUFFICIENT_BATTERY,
            "Battery level is not enough for flight, navigating to closest support point: " + closestSupportPoint.getName()
        );

        return false;
    }
}
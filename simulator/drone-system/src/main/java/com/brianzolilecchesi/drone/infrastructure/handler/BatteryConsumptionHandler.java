package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.LogConstants;

import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;

public class BatteryConsumptionHandler implements StepHandler {

    private final DroneContext context;
    private final BatteryService batteryService;
    private final LogService logService;
    private final NavigationService navigationService;

    public BatteryConsumptionHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.batteryService = droneServices.getBatteryService();
        this.logService = droneServices.getLogService();
        this.navigationService = droneServices.getNavigationService();
    }

    @Override
    public boolean handle() {
        
        if (context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) return false;

        if(batteryService.isBatteryCritical()){
            logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CRITICAL, "Battery critical level detected, requesting emergency landing.");
            context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING_REQUEST);
            return false;
        }

        if (context.getFlightMode() == DroneFlightMode.LANDING_REQUEST) return false;
        if (navigationService.getFlightPlanStatus() != DataStatus.AVAILABLE) return false;
        
        double flightDistance = navigationService.getFlightDistanceToEnd();
        if(batteryService.isBatteryEnoughForFlight(flightDistance)) return false;
        
        logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.INSUFFICIENT_BATTERY, 
                "Battery insufficient for flight distance: " + flightDistance + " meters. Current battery level: " + batteryService.getBatteryLevel() + "mAh");

        context.setFlightMode(DroneFlightMode.LANDING_REQUEST);
 
        return false;
    }
}
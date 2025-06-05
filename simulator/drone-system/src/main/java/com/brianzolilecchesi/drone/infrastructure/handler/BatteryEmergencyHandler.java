package com.brianzolilecchesi.drone.infrastructure.handler;

import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.DroneFlightMode;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryService;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.NavigationService;

public class BatteryEmergencyHandler implements StepHandler {

    private final DroneContext context;
    private final BatteryService batteryService;
    private final LogService logService;
    private final FlightController flightController;
    private final NavigationService navigationService;

    public BatteryEmergencyHandler(DroneContext ctx, DroneServiceFacade droneServices) {
        this.context = ctx;
        this.batteryService = droneServices.getBatteryService();
        this.logService = droneServices.getLogService();
        this.flightController = droneServices.getFlightController();
        this.navigationService = droneServices.getNavigationService();
    }

    @Override
    public boolean handle() {
        double level = batteryService.getBatteryLevel();
        logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CHECK, "Battery level: " + level);

        if (!batteryService.isBatteryCritical() ||
            context.getFlightMode() == DroneFlightMode.EMERGENCY_LANDING) {
            return false;
        }

        logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.BATTERY_CRITICAL, "Battery critical, emergency landing initiated");

        Position pos = flightController.getCurrentPosition();
        
        logService.info(LogConstants.Component.BATTERY_HANDLER, LogConstants.Event.EMERGENCY_LANDING, "Unsafe zone, emergency at " + pos);
        context.setFlightMode(DroneFlightMode.EMERGENCY_LANDING);
        
        navigationService.configureVerticalLanding(pos);
        return false;
    }
}
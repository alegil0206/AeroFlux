package com.brianzolilecchesi.drone;

import java.util.Collections;
import java.util.List;
import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.landing.LandingService;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.domain.service.navigation.DroneSafetyNavigationService;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.AuthorizationService;
import com.brianzolilecchesi.drone.infrastructure.service.battery.BatteryMonitor;
import com.brianzolilecchesi.drone.infrastructure.service.communication.RadioService;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.GeoZoneService;
import com.brianzolilecchesi.drone.infrastructure.service.landing.SafeLandingService;
import com.brianzolilecchesi.drone.infrastructure.service.log.StepLogService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.CollisionAvoidanceService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.FlightNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.GeozoneNavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.supportPoint.SupportPointService;
import com.brianzolilecchesi.drone.infrastructure.service.weather.WeatherService;
import com.brianzolilecchesi.drone.infrastructure.controller.FlightController;
import com.brianzolilecchesi.drone.infrastructure.handler.FlightPlanningHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.BatteryEmergencyHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.ConflictAvoidanceHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.DataAcquisitionHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.FlightControlHandler;

public class DroneSystem {

    private final DroneContext context;
    private final List<StepHandler> stepHandlers;
    
    public DroneSystem(
            DroneProperties droneProperties,
            HardwareAbstractionLayer hardwareAbstractionLayer
            ) {
        LogService logService = new StepLogService(droneProperties.getId());
        BatteryService batteryService = new BatteryMonitor(hardwareAbstractionLayer.getBattery(), logService);
        CommunicationService communicationService = new RadioService(hardwareAbstractionLayer.getRadio(), logService);
        LandingService landingService = new SafeLandingService(hardwareAbstractionLayer.getCamera(), logService);
        NavigationService navigationService = new FlightNavigationService(logService);
        GeozoneNavigationService geozoneNavigationService = new GeozoneNavigationService();
        FlightController flightController = new FlightController(hardwareAbstractionLayer.getMotor(), hardwareAbstractionLayer.getGps(), hardwareAbstractionLayer.getAltimeter(), logService);
        DroneSafetyNavigationService collisionAvoidanceService = new CollisionAvoidanceService();

        GeoZoneService geoZoneService = new GeoZoneService(logService);
        WeatherService weatherService = new WeatherService(logService);
        AuthorizationService authorizationService= new AuthorizationService();
        SupportPointService supportPointService = new SupportPointService();
                
        this.context = new DroneContext(
                droneProperties,
                hardwareAbstractionLayer,
                logService,
                flightController,
                batteryService,
                navigationService,
                landingService,
                geoZoneService,
                weatherService,
                authorizationService,
                supportPointService,
                geozoneNavigationService,
                communicationService,
                collisionAvoidanceService
        );

        this.stepHandlers = List.of(
                new DataAcquisitionHandler(),               
                new FlightPlanningHandler(),
                new BatteryEmergencyHandler(),
                new ConflictAvoidanceHandler(),
                new FlightControlHandler()
        );
    
    }

    public DroneStatus executeStep() {

        if (!context.flightController.isPoweredOn()) return null;
        
        context.logService.info(LogConstants.Component.DRONE_SYSTEM, "Step", "Executing step " + context.nextStep());

        for (StepHandler handler : stepHandlers) {
            if (handler.handle(context)) break;
        }
            
        context.communicationService.sendDroneStatus(
            new NearbyDroneStatus(
                context.props.getId(),
                context.props.getOperationCategory(),
                context.getFlightMode(),
                context.flightController.getCurrentPosition(),
                context.navigationService.getNextPositions()));
    
        return getDroneStatus();
    }

    public DroneStatus getDroneStatus() {
        return new DroneStatus(
                context.props.getId(),
                context.flightController.getCurrentPosition(),
                context.batteryService.getBatteryLevel(),
                context.navigationService.getFlightPlan() != null 
                    ? context.navigationService.getFlightPlan().getPositions()
                    : Collections.emptyList(),
                context.logService.extractLogEntries()
        );
    }

    public DroneProperties getDroneProperties() {
        return context.props;
    }

    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return context.hardwareAbstractionLayer;
    }
    
    public void powerOn() {
        context.flightController.powerOn();
    }

    public void powerOff() {
        context.flightController.powerOff();
    }

}


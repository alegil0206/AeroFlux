package com.aeroflux.drone;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aeroflux.drone.domain.handler.StepHandler;
import com.aeroflux.drone.domain.model.DroneProperties;
import com.aeroflux.drone.domain.model.DroneStatus;
import com.aeroflux.drone.domain.model.DroneContext;
import com.aeroflux.drone.domain.model.DroneFlightMode;
import com.aeroflux.drone.domain.model.NearbyDroneStatus;

import com.aeroflux.drone.domain.model.LogConstants;

import com.aeroflux.drone.infrastructure.component.HardwareAbstractionLayer;
import com.aeroflux.drone.infrastructure.service.DroneServiceFacade;

import com.aeroflux.drone.infrastructure.handler.FlightPlanningHandler;
import com.aeroflux.drone.infrastructure.handler.GeoLocationHandler;
import com.aeroflux.drone.infrastructure.handler.BatteryConsumptionHandler;
import com.aeroflux.drone.infrastructure.handler.ConflictAvoidanceHandler;
import com.aeroflux.drone.infrastructure.handler.DataAcquisitionHandler;
import com.aeroflux.drone.infrastructure.handler.FlightControlHandler;

public class DroneSystem {

    private final DroneContext context;
    private final HardwareAbstractionLayer hardwareAbstractionLayer;
    private final List<StepHandler> stepHandlers;
    private final DroneServiceFacade droneServices;
    
    public DroneSystem(
            DroneProperties droneProperties,
            HardwareAbstractionLayer hardwareAbstractionLayer,
            Map<String, String> microservicesUrlsMap
            ) {
        this.context = new DroneContext(droneProperties);
        this.hardwareAbstractionLayer = hardwareAbstractionLayer;
        this.droneServices = new DroneServiceFacade(context, hardwareAbstractionLayer, microservicesUrlsMap);
        this.stepHandlers = List.of(
                new BatteryConsumptionHandler(context, droneServices),
                new DataAcquisitionHandler(context, droneServices),
                new GeoLocationHandler(context, droneServices),          
                new FlightPlanningHandler(context, droneServices),
                new ConflictAvoidanceHandler(context, droneServices),
                new FlightControlHandler(context, droneServices)
        );
    }

    public DroneStatus executeStep() {

        if (!droneServices.getFlightController().isPoweredOn()) return null;
        
        droneServices.getLogService().info(LogConstants.Component.DRONE_SYSTEM, "Step", "Executing step " + context.nextStep());

        for (StepHandler handler : stepHandlers) {
            if (handler.handle()) break;
        }
            
        droneServices.getCommunicationService().sendDroneStatus(
            new NearbyDroneStatus(
                context.getDroneProperties().getId(),
                context.getDroneProperties().getOperationCategory(),
                context.getFlightMode(),
                droneServices.getFlightController().getCurrentPosition(),
                droneServices.getNavigationService().getNextWaypoints()));
    
        return getDroneStatus();
    }

    public DroneStatus getDroneStatus() {
        return new DroneStatus(
                context.getDroneProperties().getId(),
                droneServices.getFlightController().getCurrentPosition(),
                droneServices.getBatteryService().getBatteryLevel(),
                context.getFlightMode(),
                droneServices.getNavigationService().getFlightPlan() != null 
                    ? droneServices.getNavigationService().getFlightPlan().getPositions()
                    : Collections.emptyList(),
                droneServices.getLogService().extractLogEntries()
        );
    }

    public DroneProperties getDroneProperties() {
        return context.getDroneProperties();
    }

    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return hardwareAbstractionLayer;
    }
    
    public void powerOn() {
        droneServices.getFlightController().powerOn();
        context.setFlightMode(DroneFlightMode.NORMAL_FLIGHT);
    }

    public void powerOff() {
        droneServices.getFlightController().powerOff();
    }

}


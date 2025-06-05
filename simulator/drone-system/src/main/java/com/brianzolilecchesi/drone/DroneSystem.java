package com.brianzolilecchesi.drone;

import java.util.Collections;
import java.util.List;
import com.brianzolilecchesi.drone.domain.handler.StepHandler;
import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.DroneStatus;
import com.brianzolilecchesi.drone.domain.model.DroneContext;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;

import com.brianzolilecchesi.drone.domain.model.LogConstants;

import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.infrastructure.service.DroneServiceFacade;

import com.brianzolilecchesi.drone.infrastructure.handler.FlightPlanningHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.BatteryConsumptionHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.ConflictAvoidanceHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.DataAcquisitionHandler;
import com.brianzolilecchesi.drone.infrastructure.handler.FlightControlHandler;

public class DroneSystem {

    private final DroneContext context;
    private final HardwareAbstractionLayer hardwareAbstractionLayer;
    private final List<StepHandler> stepHandlers;
    private final DroneServiceFacade droneServices;
    
    public DroneSystem(
            DroneProperties droneProperties,
            HardwareAbstractionLayer hardwareAbstractionLayer,
            double droneMaxSpeed
            ) {
        this.context = new DroneContext(droneProperties, droneMaxSpeed);
        this.hardwareAbstractionLayer = hardwareAbstractionLayer;
        this.droneServices = new DroneServiceFacade(context, hardwareAbstractionLayer);
        this.stepHandlers = List.of(
                new BatteryConsumptionHandler(context, droneServices),
                new DataAcquisitionHandler(context, droneServices),               
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
    }

    public void powerOff() {
        droneServices.getFlightController().powerOff();
    }

}


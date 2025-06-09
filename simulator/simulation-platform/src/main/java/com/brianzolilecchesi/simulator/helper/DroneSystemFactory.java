package com.brianzolilecchesi.simulator.helper;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.model.drone.SimulatedBattery;
import com.brianzolilecchesi.simulator.model.drone.SimulatedRadio;
import com.brianzolilecchesi.simulator.service.api.DroneIdentificationService;
import com.brianzolilecchesi.simulator.model.drone.SimulatedGPS;
import com.brianzolilecchesi.simulator.model.drone.SimulatedMotor;
import com.brianzolilecchesi.simulator.model.drone.SimulatedAltimeter;

import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.infrastructure.component.HardwareAbstractionLayer;
import com.brianzolilecchesi.drone.domain.model.AdaptiveCapabilities;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.simulator.dto.DronePropertiesDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DroneSystemFactory {

    private final DroneIdentificationService droneIdentificationService;

    public DroneSystemFactory(DroneIdentificationService droneIdentificationService) {
        this.droneIdentificationService = droneIdentificationService;
    }

    public List<DroneSystem> createDrones() {
        List<DronePropertiesDTO> droneDTOs = droneIdentificationService.getDrones();
        List<DroneSystem> drones = new ArrayList<>();
        
        for (DronePropertiesDTO droneDTO : droneDTOs) {
            SimulatedBattery battery = new SimulatedBattery(droneDTO.getBattery());
            SimulatedRadio radio = new SimulatedRadio();
            SimulatedGPS gps = new SimulatedGPS(droneDTO.getSource().getLatitude(), droneDTO.getSource().getLongitude());
            SimulatedAltimeter altimeter = new SimulatedAltimeter(0);
            SimulatedMotor motor = new SimulatedMotor(gps, altimeter);
            DroneProperties droneProperties = new DroneProperties(
                droneDTO.getId(),
                droneDTO.getName(),
                droneDTO.getModel(),
                droneDTO.getOwner(),
                droneDTO.getOperationCategory(),
                droneDTO.getPlanDefinitionTimestamp(),
                new AdaptiveCapabilities(
                    droneDTO.getAdaptiveCapabilities().getSafeLanding(),
                    droneDTO.getAdaptiveCapabilities().getCollisionAvoidance(),
                    droneDTO.getAdaptiveCapabilities().getGeoAwareness(),
                    droneDTO.getAdaptiveCapabilities().getAutoAuthorization(),
                    droneDTO.getAdaptiveCapabilities().getBatteryManagement()
                ),
                droneDTO.getBattery(),
                new Coordinate(
                    droneDTO.getSource().getLatitude(),
                    droneDTO.getSource().getLongitude()
                ),
                new Coordinate(
                    droneDTO.getDestination().getLatitude(),
                    droneDTO.getDestination().getLongitude()
                )
            );
            HardwareAbstractionLayer hardwareAbstractionLayer = new HardwareAbstractionLayer(
                battery,
                radio,
                gps,
                altimeter,
                motor
            );

            DroneSystem drone = new DroneSystem(droneProperties, hardwareAbstractionLayer);
            drone.powerOn();
            drones.add(drone);
        }

        for (DroneSystem drone : drones) {
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setSender(drone);
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setAvailableDrone(drones);
        }

        return drones;
    }
}
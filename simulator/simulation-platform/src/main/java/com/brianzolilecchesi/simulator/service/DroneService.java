package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.model.drone.SimulatedBattery;
import com.brianzolilecchesi.simulator.model.drone.SimulatedCamera;
import com.brianzolilecchesi.simulator.model.drone.SimulatedRadio;
import com.brianzolilecchesi.simulator.model.drone.SimulatedGPS;
import com.brianzolilecchesi.simulator.model.drone.SimulatedMotor;
import com.brianzolilecchesi.simulator.model.drone.SimulatedAltimeter;

import com.brianzolilecchesi.drone.domain.model.DroneProperties;
import com.brianzolilecchesi.drone.domain.model.AdaptiveCapabilities;
import com.brianzolilecchesi.drone.domain.model.Coordinates;
import com.brianzolilecchesi.simulator.dto.DronePropertiesDTO;
import com.brianzolilecchesi.simulator.service.api.DroneIdentificationService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
class DroneService {
    private final List<DroneSystem> drones = new CopyOnWriteArrayList<>();
    private final LogService logService;
    private final DroneIdentificationService droneIdentificationService;

    public DroneService(LogService logService, DroneIdentificationService droneIdentificationService) {
        this.logService = logService;
        this.droneIdentificationService = droneIdentificationService;
    }

    public List<DroneSystem> createDrones() {
        drones.clear();
        List<DronePropertiesDTO> droneDTOs =  droneIdentificationService.getAllDrones();
        for (DronePropertiesDTO droneDTO : droneDTOs) {
            SimulatedBattery battery = new SimulatedBattery(droneDTO.getBattery());
            SimulatedRadio radio = new SimulatedRadio();
            SimulatedCamera camera = new SimulatedCamera();
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
                new Coordinates(
                    droneDTO.getSource().getLatitude(),
                    droneDTO.getSource().getLongitude()
                ),
                new Coordinates(
                    droneDTO.getDestination().getLatitude(),
                    droneDTO.getDestination().getLongitude()
                )
            );

            DroneSystem drone = new DroneSystem(droneProperties, battery, radio, camera, gps, altimeter, motor);

            drone.addPropertyChangeListener(evt -> {
                if ("message".equals(evt.getPropertyName())) {
                    logService.sendLog("INFO", (String) evt.getNewValue());
                }
            });
            drones.add(drone);
        }

        for (DroneSystem drone : drones) {
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setSender(drone);
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setAvailableDrone(drones);
        }


        return drones;
    }

    public void clearDrones() {
        drones.clear();
    }
}
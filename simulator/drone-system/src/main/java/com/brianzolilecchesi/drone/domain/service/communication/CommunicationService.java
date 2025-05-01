package com.brianzolilecchesi.drone.domain.service.communication;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;

public interface CommunicationService {
    void sendDroneStatus(NearbyDroneStatus droneStatus);
    List<NearbyDroneStatus> getNearbyDroneStatus();
}
package com.brianzolilecchesi.drone.infrastructure.service.communication;

import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.dto.RadioMessageDTO;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;
import com.brianzolilecchesi.drone.domain.model.LogConstants;

import java.util.List;

public class CommunicationService {

    private Radio radio;
    private LogService logService;

    public CommunicationService(Radio radio, LogService logService) {
        this.radio = radio;
        this.logService = logService;
    }

    public void sendDroneStatus(NearbyDroneStatus droneStatus) {
        RadioMessageDTO radioMessage = new RadioMessageDTO(
                droneStatus.getDroneId(),
                droneStatus.getOperationCategory(),
                droneStatus.getFlightMode(),
                droneStatus.getPosition(),
                droneStatus.getNextPositions()
        );
        radio.sendMessage(radioMessage);
    }

    public List<NearbyDroneStatus> getNearbyDroneStatus() {
        List<RadioMessageDTO> messages = radio.getReceivedMessages();
        List<NearbyDroneStatus> nearbyDroneStatuses = messages.stream()
                .map(message -> new NearbyDroneStatus(
                        message.getDroneId(),
                        message.getOperationCategory(),
                        message.getFlightMode(),
                        message.getPosition(),
                        message.getNextPositions()))
                .toList();
        for (NearbyDroneStatus status : nearbyDroneStatuses) {
            logService.info(LogConstants.Component.COMMUNICATION_SERVICE, LogConstants.Event.MESSAGE_RECEIVED, status.toString());
        }
        return nearbyDroneStatuses;
    }
}

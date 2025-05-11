package com.brianzolilecchesi.drone.infrastructure.service.communication;

import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.dto.RadioMessageDTO;
import com.brianzolilecchesi.drone.domain.model.NearbyDroneStatus;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.model.LogConstants;

import java.util.List;

public class RadioService implements CommunicationService {

    private Radio radio;
    private LogService logService;

    public RadioService(Radio radio, LogService logService) {
        this.radio = radio;
        this.logService = logService;
    }

    @Override
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

    @Override
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

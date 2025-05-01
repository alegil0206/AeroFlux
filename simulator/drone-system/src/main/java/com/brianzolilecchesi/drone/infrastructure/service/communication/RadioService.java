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
                droneStatus.isEmergency(),
                droneStatus.getPosition(),
                droneStatus.getNextPosition()
        );
        radio.sendMessage(radioMessage);
    }

    @Override
    public List<NearbyDroneStatus> getNearbyDroneStatus() {
        List<RadioMessageDTO> messages = radio.getReceivedMessages();
        List<NearbyDroneStatus> nearbyDroneStatuses = messages.stream()
                .map(message -> new NearbyDroneStatus(
                        message.getDroneId(),
                        message.isEmergency(),
                        message.getPosition(),
                        message.getNexPosition()))
                .toList();
        for (NearbyDroneStatus status : nearbyDroneStatuses) {
            logService.info(LogConstants.Service.COMMUNICATION_SERVICE, "Communication Received", status.toString());
        }
        return nearbyDroneStatuses;
    }
}

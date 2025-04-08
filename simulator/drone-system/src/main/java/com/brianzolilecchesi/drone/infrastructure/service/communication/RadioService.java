package com.brianzolilecchesi.drone.infrastructure.service.communication;

import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;
import com.brianzolilecchesi.drone.domain.service.log.LogService;

import java.util.List;

public class RadioService implements CommunicationService {

    private Radio radio;
    private LogService logService;

    public RadioService(Radio radio, LogService logService) {
        this.radio = radio;
        this.logService = logService;
    }

    @Override
    public void sendMessage(String message) {
        radio.sendMessage(message);
    }

    @Override
    public List<String> getMessages() {
        return radio.getReceivedMessages();
    }
}

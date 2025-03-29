package com.brianzolilecchesi.drone.infrastructure.service.communication;

import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.domain.service.communication.CommunicationService;

import java.util.List;

public class RadioService implements CommunicationService {

    private Radio radio;

    public RadioService(Radio radio) {
        this.radio = radio;
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

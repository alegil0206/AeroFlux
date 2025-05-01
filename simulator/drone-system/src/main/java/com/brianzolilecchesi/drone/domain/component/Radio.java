package com.brianzolilecchesi.drone.domain.component;

import java.util.List;

import com.brianzolilecchesi.drone.domain.dto.RadioMessageDTO;

public interface Radio {
    void sendMessage(RadioMessageDTO message);
    List<RadioMessageDTO> getReceivedMessages();
}
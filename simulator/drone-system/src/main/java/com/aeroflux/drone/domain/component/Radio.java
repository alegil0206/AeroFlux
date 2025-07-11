package com.aeroflux.drone.domain.component;

import java.util.List;

import com.aeroflux.drone.domain.dto.RadioMessageDTO;

public interface Radio {
    void sendMessage(RadioMessageDTO message);
    List<RadioMessageDTO> getReceivedMessages();
}
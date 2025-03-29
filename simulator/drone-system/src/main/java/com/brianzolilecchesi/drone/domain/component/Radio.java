package com.brianzolilecchesi.drone.domain.component;

import java.util.List;

public interface Radio {
    void sendMessage(String message);
    List<String> getReceivedMessages();
}
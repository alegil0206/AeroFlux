package com.brianzolilecchesi.drone.domain.service.communication;

import java.util.List;

public interface CommunicationService {
    void sendMessage(String message);
    List<String> getMessages();
}
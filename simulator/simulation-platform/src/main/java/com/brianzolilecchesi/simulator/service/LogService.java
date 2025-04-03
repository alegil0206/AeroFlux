package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.dto.DroneStatusDTO;
import com.brianzolilecchesi.simulator.dto.LogDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogService {
    private final SimpMessagingTemplate messagingTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLog(String category, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        LogDTO log = new LogDTO(category, message, timestamp);
        messagingTemplate.convertAndSend("/topic/logs", log);
    }

    public void sendDroneStatus(DroneStatusDTO droneStatus) {
        messagingTemplate.convertAndSend("/topic/drone-status", droneStatus);
    }
}
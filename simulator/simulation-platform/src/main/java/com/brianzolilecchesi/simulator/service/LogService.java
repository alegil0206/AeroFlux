package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.domain.model.LogEntry;
import com.brianzolilecchesi.simulator.dto.DroneStatusDTO;
import com.brianzolilecchesi.simulator.dto.LogDTO;
import com.brianzolilecchesi.simulator.model.Constants;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class LogService {
    private final SimpMessagingTemplate messagingTemplate;
    private final List<LogEntry> logEntries = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    public LogService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void log(String level, String component, String event, String message) {
        String dateTime = java.time.LocalDateTime.now().format(formatter);
        LogEntry logEntry = new LogEntry(Constants.System.SIMULATOR, level, component, event, message, dateTime);
        logEntries.add(logEntry);
        System.out.println(logEntry);
        sendLog(logEntry);
    }

    public void info(String component, String event, String message) {
        log(Constants.Level.INFO, component, event, message);
    }

    public void registerLogEntries(List<LogEntry> logs) {
        logEntries.addAll(logs);
        for (LogEntry log : logs) {
            sendLog(log);
        }
    }

    private void sendLog(LogEntry logEntry) {
        LogDTO log = new LogDTO(logEntry);
        messagingTemplate.convertAndSend("/topic/logs", log);
    }

    public void sendDroneStatus(DroneStatusDTO droneStatus) {
        messagingTemplate.convertAndSend("/topic/drone-status", droneStatus);
    }

    public List<LogDTO> getAndClearLogEntries () {
        List<LogDTO> logs = new ArrayList<>();
        for (LogEntry logEntry : logEntries) {
            logs.add(new LogDTO(logEntry));
        }
        logEntries.clear();
        return logs;
    }
}
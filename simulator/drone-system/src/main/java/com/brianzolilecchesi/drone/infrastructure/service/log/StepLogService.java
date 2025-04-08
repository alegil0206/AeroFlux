package com.brianzolilecchesi.drone.infrastructure.service.log;

import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.model.LogEntry;

public class StepLogService implements LogService {
    private final String droneId;
    private final List<LogEntry> logEntries = new ArrayList<>();

    public StepLogService(String droneId) {
        this.droneId = droneId;
    }

    public void log(String level, String component, String event, String message) {
        String dateTime = java.time.LocalDateTime.now().toString();
        LogEntry entry = new LogEntry(droneId, level, component, event, message, dateTime);
        logEntries.add(entry);
        System.out.println(entry);
    }

    public List<LogEntry> getLogEntries() {
        return new ArrayList<>(logEntries);
    }

    public void clear() {
        logEntries.clear();
    }
}

package com.aeroflux.drone.infrastructure.service.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.domain.model.LogEntry;

public class LogService {
    private final String systemId;
    private final List<LogEntry> logEntries = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public LogService(String systemId) {
        this.systemId = systemId;
    }

    public void log(String level, String component, String event, String message) {
        String dateTime = LocalDateTime.now().format(formatter);
        LogEntry entry = new LogEntry(systemId, level, component, event, message, dateTime);
        System.out.println(entry);
        logEntries.add(entry);
    }

    public void info(String component, String event, String message) {
        log(LogConstants.Level.INFO, component, event, message);
    }

    public List<LogEntry> getLogEntries() {
        return new ArrayList<>(logEntries);
    }

    public List<LogEntry> extractLogEntries() {
        List<LogEntry> logToSend = getLogEntries();
        logEntries.clear();
        return logToSend;
    }

    public void clear() {
        logEntries.clear();
    }
}

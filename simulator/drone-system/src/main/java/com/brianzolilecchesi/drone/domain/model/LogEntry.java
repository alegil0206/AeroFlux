package com.brianzolilecchesi.drone.domain.model;

public class LogEntry {
    private final String systemId;
    private final String level;
    private final String component;
    private final String event;
    private final String message;
    private final String timestamp;

    public LogEntry(String systemId, String level, String component, String event, String message, String timestamp) {
        this.systemId = systemId;
        this.level = level;
        this.component = component;
        this.event = event;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getLevel() {
        return level;
    }

    public String getComponent() {
        return component;
    }

    public String getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [%s] [%s] [%s] %s - %s | %s",
            timestamp, level, systemId, component, event, message, timestamp != null ? timestamp : ""
        );
    }
}

package com.brianzolilecchesi.drone.domain.model;

import java.time.Instant;

public class LogEntry {
    private final Instant timestamp;
    private final String droneId;
    private final String level;
    private final String component;
    private final String event;
    private final String message;
    private final String dateTime;

    public LogEntry(String droneId, String level, String component, String event, String message, String dateTime) {
        this.timestamp = Instant.now();
        this.droneId = droneId;
        this.level = level;
        this.component = component;
        this.event = event;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getDroneId() {
        return droneId;
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

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [%s] [%s] [%s] %s - %s | %s",
            timestamp, level, droneId, component, event, message, dateTime != null ? dateTime : ""
        );
    }
}

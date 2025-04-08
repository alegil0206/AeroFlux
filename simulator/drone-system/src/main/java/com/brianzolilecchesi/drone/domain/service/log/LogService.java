package com.brianzolilecchesi.drone.domain.service.log;

import java.util.List;

import com.brianzolilecchesi.drone.domain.model.LogEntry;

public interface LogService {
    public void log(String level, String component, String event, String message);
    public List<LogEntry> getLogEntries();
    public void clear();
}

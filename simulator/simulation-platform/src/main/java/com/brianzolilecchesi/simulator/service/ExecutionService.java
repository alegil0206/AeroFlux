package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.model.ExecutionState;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class ExecutionService {
    private final ExecutionState executionState = new ExecutionState();
    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;
    private final LogService logService;

    public ExecutionService(LogService logService) {
        this.logService = logService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.initialize();
    }

    public String start() {
        if (executionState.isRunning() || executionState.isPaused())
            stop(); // Se già in esecuzione, resetta
        executionState.setRunning(true);
        executionState.setPaused(false);
        executionState.resetCycle();
        scheduleExecutionLoop();
        logService.sendLog("INFO", "Execution started");
        return executionState.getStatus();
    }

    public String stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        executionState.setRunning(false);
        executionState.setPaused(false);
        executionState.resetCycle();
        logService.sendLog("INFO", "Execution stopped");
        return executionState.getStatus();
    }

    public String pause() {
        if (executionState.isRunning() && !executionState.isPaused()) {
            executionState.setPaused(true);
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
            }
        }
        logService.sendLog("INFO", "Execution paused");
        return executionState.getStatus();
    }

    public String resume() {
        if (executionState.isRunning() && executionState.isPaused()) {
            executionState.setPaused(false);
            scheduleExecutionLoop();
        }
        logService.sendLog("INFO", "Execution resumed");
        return executionState.getStatus();
    }

    public String getStatus() {
        return executionState.getStatus();
    }

    private void scheduleExecutionLoop() {
        scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
            if (!executionState.isPaused()) {
                executionState.incrementCycle();
                String message = "Ho fatto il " + executionState.getCycleCount() + "-esimo ciclo di esecuzione";
                logService.sendLog("INFO", message);
            }
        }, 2000);
    }
}

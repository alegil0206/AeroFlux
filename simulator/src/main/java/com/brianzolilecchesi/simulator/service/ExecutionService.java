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

    public void start() {
        stop(); // Se già in esecuzione, resetta
        executionState.setRunning(true);
        executionState.setPaused(false);
        executionState.resetCycle();
        scheduleExecutionLoop();
        logService.sendLog("INFO", "Execution started");
    }

    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
        executionState.setRunning(false);
        executionState.setPaused(false);
        executionState.resetCycle();
        logService.sendLog("INFO", "Execution stopped");
    }

    public void pause() {
        if (executionState.isRunning() && !executionState.isPaused()) {
            executionState.setPaused(true);
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
            }
        }
        logService.sendLog("INFO", "Execution paused");
    }

    public void resume() {
        if (executionState.isRunning() && executionState.isPaused()) {
            executionState.setPaused(false);
            scheduleExecutionLoop();
        }
        logService.sendLog("INFO", "Execution resumed");
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
        }, 1000);
    }
}

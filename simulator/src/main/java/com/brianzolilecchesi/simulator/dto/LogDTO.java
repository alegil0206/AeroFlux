package com.brianzolilecchesi.simulator.dto;

public class LogDTO {
    private String category;
    private String message;
    private String timestamp;

    public LogDTO() {}

    public LogDTO(String category, String message, String timestamp) {
        this.category = category;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

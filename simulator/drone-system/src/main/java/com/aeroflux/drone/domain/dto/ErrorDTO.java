package com.aeroflux.drone.domain.dto;

public class ErrorDTO {
    private String message;

    public ErrorDTO(String message) {
        this.message = message;
    }
    
	public ErrorDTO(Exception e) {
		this(e.getMessage());
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
	public String toString() {
		return message;
	}
}

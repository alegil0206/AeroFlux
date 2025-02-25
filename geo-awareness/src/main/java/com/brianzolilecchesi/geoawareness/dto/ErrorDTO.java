package com.brianzolilecchesi.geoawareness.dto;

public class ErrorDTO {
	
    private String message;

    public ErrorDTO(String error) {
        this.message = error;
    }
    
	public ErrorDTO(Exception e) {
		this(e.getMessage());
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String error) {
        this.message = error;
    }
    
    @Override
	public String toString() {
		return message;
	}
}
package com.aeroflux.drone.domain.exception;

public class ExternalServiceException extends Exception {
    private String errorMessage;

    public ExternalServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

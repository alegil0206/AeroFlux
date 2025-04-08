package com.brianzolilecchesi.drone.domain.exception;

public class AuthorizationException extends Exception {
    private String errorMessage;

    public AuthorizationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
package com.aeroflux.geoauthorization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.aeroflux.geoauthorization.dto.ErrorDTO;
import com.aeroflux.geoauthorization.exception.BadRequestException;
import com.aeroflux.geoauthorization.exception.ForbiddenException;
import com.aeroflux.geoauthorization.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorDTO(e.getMessage()));
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorDTO(e.getMessage()));
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(ForbiddenException e) {
        return ResponseEntity
        		.status(HttpStatus.FORBIDDEN)
        		.body(new ErrorDTO(e.getMessage()));
            
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorDTO("Internal server error: " + e.getMessage()));
    }
}
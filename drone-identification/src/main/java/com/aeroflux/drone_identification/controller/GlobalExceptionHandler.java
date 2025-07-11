package com.aeroflux.drone_identification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aeroflux.drone_identification.dto.ErrorDTO;
import com.aeroflux.drone_identification.exception.BadRequestException;
import com.aeroflux.drone_identification.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleIllegalGeozoneException(BadRequestException e) {
        return ResponseEntity
        		.badRequest()
        		.body(new ErrorDTO(e));
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleDroneNotFoundException(NotFoundException e) {
        return ResponseEntity
        		.notFound()
        		.build();
    }
}
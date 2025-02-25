package com.brianzolilecchesi.geoawareness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.brianzolilecchesi.geoawareness.dto.ErrorDTO;
import com.brianzolilecchesi.geoawareness.exception.BadRequestException;
import com.brianzolilecchesi.geoawareness.exception.ForbiddenException;
import com.brianzolilecchesi.geoawareness.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleIllegalGeozoneException(final BadRequestException e) {
        return ResponseEntity
        		.badRequest()
        		.body(new ErrorDTO(e));
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleGeozoneNotFoundException(final NotFoundException e) {
        return ResponseEntity
        		.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDTO> handleForbiddenGeozoneException(final ForbiddenException e) {
        return ResponseEntity
        		.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO(e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> handleException(final Exception e) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorDTO(e.getMessage()));
	}
}
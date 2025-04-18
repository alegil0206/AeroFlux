package com.brianzolilecchesi.geoawareness.exception.supportPoint;

import com.brianzolilecchesi.geoawareness.exception.BadRequestException;
import com.brianzolilecchesi.geoawareness.dto.SupportPointDTO;

public class IllegalSupportPointException extends BadRequestException {

    private static final long serialVersionUID = 1L;

    public IllegalSupportPointException() {
        super("Illegal support point: no support point provided");
    }

    public IllegalSupportPointException(final String message) {
        super(message);
    }

    public IllegalSupportPointException(final SupportPointDTO supportPointDTO) {
        super("Illegal support point: " + supportPointDTO.toString());
    }
    
}

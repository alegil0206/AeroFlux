package com.aeroflux.geoawareness.exception.supportPoint;

import com.aeroflux.geoawareness.exception.BadRequestException;
import com.aeroflux.geoawareness.dto.SupportPointDTO;

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

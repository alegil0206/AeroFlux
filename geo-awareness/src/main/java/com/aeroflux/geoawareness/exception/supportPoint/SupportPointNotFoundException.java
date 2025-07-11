package com.aeroflux.geoawareness.exception.supportPoint;

import com.aeroflux.geoawareness.exception.NotFoundException;

public class SupportPointNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = 1L;
    
    public SupportPointNotFoundException() {
        super();
    }
    
    public SupportPointNotFoundException(String id) {
        super(String.format("Support point with id %s not found", id));
    }
}

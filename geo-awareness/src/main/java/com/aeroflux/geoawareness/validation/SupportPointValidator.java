package com.aeroflux.geoawareness.validation;

import com.aeroflux.geoawareness.dto.SupportPointDTO;
import com.aeroflux.geoawareness.exception.supportPoint.IllegalSupportPointException;

public class SupportPointValidator {

    private Range longitudeRange;
    private Range latitudeRange;

    public SupportPointValidator(
            final Range longitudeRange,
            final Range latitudeRange
    ) {
        this.longitudeRange = longitudeRange;
        this.latitudeRange = latitudeRange;
    }

    public Range getLongitudeRange() {
        return longitudeRange;
    }

    public void setLongitudeRange(final Range longitudeRange) {
        this.longitudeRange = longitudeRange;
    }

    public Range getLatitudeRange() {
        return latitudeRange;
    }

    public void setLatitudeRange(final Range latitudeRange) {
        this.latitudeRange = latitudeRange;
    }

    public void validate(final SupportPointDTO supportPoint) throws IllegalSupportPointException {
        if (supportPoint == null) {
            throw new IllegalSupportPointException(supportPoint);
        }

        if (!getLongitudeRange().isWithinRange(supportPoint.getLongitude())) {
            throw new IllegalSupportPointException(supportPoint);
        }

        if (!getLatitudeRange().isWithinRange(supportPoint.getLatitude())) {
            throw new IllegalSupportPointException(supportPoint);
        }
    }

    @Override
    public String toString() {
        return "SupportPointValidator{" +
                "longitudeRange=" + longitudeRange +
                ", latitudeRange=" + latitudeRange +
                '}';
    }
    
}

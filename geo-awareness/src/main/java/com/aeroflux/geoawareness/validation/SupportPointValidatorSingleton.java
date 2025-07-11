package com.aeroflux.geoawareness.validation;

import com.aeroflux.geoawareness.util.Constants;

public class SupportPointValidatorSingleton {

    private static SupportPointValidator instance;

    private SupportPointValidatorSingleton() {
    }

    public static SupportPointValidator getInstance() {
        if (instance == null) {
            instance = new SupportPointValidator(
                Constants.LONGITUDE_RANGE, 
                Constants.LATITUDE_RANGE
            );
        }

        return instance;
    }
}

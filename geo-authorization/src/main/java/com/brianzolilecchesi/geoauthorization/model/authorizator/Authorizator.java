package com.brianzolilecchesi.geoauthorization.model.authorizator;

import java.time.LocalDateTime;

import com.brianzolilecchesi.geoauthorization.exception.authorization.forbidden.GeozoneInactiveException;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.Authorization;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationFactorySingleton;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.Duration;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.brianzolilecchesi.geoauthorization.util.Constants;

public class Authorizator {
	
	private double specificProbability;
	private double certifiedProbability;
	
	Authorizator(double specificProbability, double certifiedProbability) {
		assert specificProbability >= 0 && specificProbability <= 1;
		assert certifiedProbability >= 0 && certifiedProbability <= 1;
		
		this.specificProbability = specificProbability;
		this.certifiedProbability = certifiedProbability;
	}
		
	private Authorization createAuthorization(
			final Drone drone, 
			final Geozone geozone, 
			final Duration duration
			) {	
		
		assert drone != null;
		assert geozone != null;
		assert duration != null;
		
		double threshold = 0;
		switch (drone.getOperationCategory()) {
		case Constants.DRONE_SPECIFIC_OPERATION_CATEGORY:
			threshold = this.specificProbability;
			break;
		case Constants.DRONE_CERTIFIED_OPERATION_CATEGORY:
			threshold = this.certifiedProbability;
			break;
		default:
            throw new IllegalArgumentException(String.format(
            		"Illegal drone operation category: %s. This should never happen!",
                   drone.getOperationCategory()));
		}
		 		
		Authorization authorization = null;
		double random = Math.random();
		if (random <= threshold) {
			authorization = AuthorizationFactorySingleton.getInstance().createAuthorizationGranted(
							drone,
							geozone,
							duration,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMinutes(duration.getTime())
                            );
		} else {
			authorization = AuthorizationFactorySingleton.getInstance().createAuthorizationDenied(
							drone,
							geozone,
							duration,
                            LocalDateTime.now(),
                            String.format(
                            		"Authorization denied by probabilistic model: sampled %f with threshold equal to %f",
                            		random,
                            		threshold
                            		)
                            );
		}
		
		return authorization;
	}
	
	public Authorization authorize(
			final Drone drone, 
			final Geozone geozone, 
			final Duration duration
			) throws GeozoneInactiveException {
		
		assert drone != null;
		assert geozone != null;
		assert duration != null;
		
		if (!geozone.isActive()) {
			throw new GeozoneInactiveException(String.format(
					"Geozone %s is inactive: no authorization required",
					geozone.getId()
					));
		}
		
		switch (geozone.getCategory()) {
			case Constants.GEOZONE_EXCLUDED_CATEGORY:
				return AuthorizationFactorySingleton.getInstance().createAuthorizationDenied(
						drone,
						geozone,
						duration,
                        LocalDateTime.now(),
                        "Request of authorization to an excluded geozone"
                        );
						            
            case Constants.GEOZONE_RESTRICTED_CATEGORY:
            	return createAuthorization(drone, geozone, duration);
		}
		
		throw new IllegalArgumentException(
				String.format(
						"Illegal geozone category: %s. This should never happen!",
						geozone.getCategory()
                        ));
	}
}
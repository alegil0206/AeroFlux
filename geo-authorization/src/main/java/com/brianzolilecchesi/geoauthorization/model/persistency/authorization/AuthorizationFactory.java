package com.brianzolilecchesi.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.brianzolilecchesi.geoauthorization.dto.AuthorizationDTO;
import com.brianzolilecchesi.geoauthorization.dto.AuthorizationDeniedDTO;
import com.brianzolilecchesi.geoauthorization.dto.AuthorizationExpiredDTO;
import com.brianzolilecchesi.geoauthorization.dto.AuthorizationGrantedDTO;
import com.brianzolilecchesi.geoauthorization.dto.AuthorizationRevokedDTO;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;

public class AuthorizationFactory {
	
	AuthorizationFactory() {
	}
	
	public AuthorizationGranted createAuthorizationGranted(
			final Drone drone,
            final Geozone geozone,
            final Duration duration,
            final LocalDateTime createdAt,
            final LocalDateTime expiresAt
            ) {
		assert drone != null;
		assert geozone != null;
		assert duration != null;
        
        return new AuthorizationGranted(drone, geozone, duration, createdAt, expiresAt);
	}
	
	public AuthorizationDenied createAuthorizationDenied(
			final Drone drone, 
			final Geozone geozone,
			final Duration duration,
			final LocalDateTime createdAt,
			final String rational
			) {
		assert drone != null;
		assert geozone != null;
		assert duration != null;
		
		return new AuthorizationDenied(drone, geozone, duration, createdAt, rational);
	}
	
	public AuthorizationExpired createAuthorizationExpired(final AuthorizationGranted expired) {
		assert expired != null;

		return new AuthorizationExpired(
				expired.getId(),
				expired.getDrone(), 
				expired.getGeozone(), 
				expired.getDuration(), 
				expired.getCreatedAt(),
				expired.getExpiresAt()
				);
	}
	
	public AuthorizationRevoked createAuthorizationRevoked(final AuthorizationGranted revoked) {
		assert revoked != null;

		return new AuthorizationRevoked(
				revoked.getId(),
				revoked.getDrone(), 
				revoked.getGeozone(), 
				revoked.getDuration(), 
				revoked.getCreatedAt(),
				revoked.getExpiresAt(),
				LocalDateTime.now()
				);
	}
	
	public AuthorizationDTO createAuthorizationDTO(final Authorization authorization) {
		assert authorization != null;
		assert authorization.getDrone() != null;
		assert authorization.getGeozone() != null;
		assert authorization.getDuration() != null;
		
		if (authorization instanceof AuthorizationGranted) {
			AuthorizationGranted granted = (AuthorizationGranted) authorization;
			return new AuthorizationGrantedDTO(
					granted.getId(), 
					granted.getDrone().getId(),
					granted.getGeozone().getCategory(),
					granted.getGeozone().getId(), 
					granted.getGeozone().getCategory(),
					granted.getDuration().getName(),
					granted.getDuration().getTime(),
					granted.getCreatedAt().toString(), 
					granted.getExpiresAt().toString()
					);
		} 
		if (authorization instanceof AuthorizationDenied) {
			AuthorizationDenied denied = (AuthorizationDenied) authorization;
			return new AuthorizationDeniedDTO(
					denied.getId(), 
					denied.getDrone().getId(), 
					denied.getGeozone().getCategory(),
					denied.getGeozone().getId(), 
					denied.getGeozone().getCategory(), 
					denied.getDuration().getName(),
					denied.getDuration().getTime(), 
					denied.getCreatedAt().toString(), 
					denied.getReason()
					);
		}
		if (authorization instanceof AuthorizationExpired) {
			AuthorizationExpired expired = (AuthorizationExpired) authorization;
			return new AuthorizationExpiredDTO(
					expired.getId(), 
					expired.getDrone().getId(), 
					expired.getGeozone().getCategory(),
					expired.getGeozone().getId(), 
					expired.getGeozone().getCategory(), 
					expired.getDuration().getName(),
					expired.getDuration().getTime(), 
					expired.getCreatedAt().toString(),
					expired.getExpiredAt().toString()
					);
		}
		if (authorization instanceof AuthorizationRevoked) {
			AuthorizationRevoked revoked = (AuthorizationRevoked) authorization;
			return new AuthorizationRevokedDTO(
					revoked.getId(), 
					revoked.getDrone().getId(), 
					revoked.getGeozone().getCategory(),
					revoked.getGeozone().getId(), 
					revoked.getGeozone().getCategory(), 
					revoked.getDuration().getName(),
					revoked.getDuration().getTime(), 
					revoked.getCreatedAt().toString(),
					revoked.getExpiredAt().toString(),
					revoked.getRevokedAt().toString()
					);
		}
		
		throw new IllegalArgumentException("Unknown authorization type: this should never happen!");
	} 
}
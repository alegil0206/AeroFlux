package com.aeroflux.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;
import com.aeroflux.geoauthorization.util.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AuthorizationRevoked extends Authorization {
	
	@Id
	private Long id;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime expiredAt;
	@Column(nullable = false, updatable = false)
	private LocalDateTime revokedAt;
	
	AuthorizationRevoked(
			final Long id,
			final Drone drone, 
			final Geozone geozone, 
			final Duration duration,
			final LocalDateTime createdAt, 
			final LocalDateTime expiredAt, 
			final LocalDateTime revokedAt
			) {
		super(drone, geozone, duration, createdAt);
		setId(id);
		setExpiredAt(expiredAt);
		setRevokedAt(revokedAt);
	}
	
	AuthorizationRevoked() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}
	
	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}
	
	public LocalDateTime getRevokedAt() {
		return revokedAt;
	}
	
	public void setRevokedAt(LocalDateTime revokedAt) {
		this.revokedAt = revokedAt;
	}
	
	@Override
	public boolean isValid() {
		return false;
	}
	
	@Override
	public String getStatus() {
		return Constants.AUTHORIZATION_REVOKED_STATUS;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationRevoked[%s, id=%s, expiresAt=%s, revokedAt=%s]",
				super.toString(), id, expiredAt, revokedAt
				);
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthorizationRevoked that = (AuthorizationRevoked) obj;
        return id != null && id.equals(that.id);
    }
}
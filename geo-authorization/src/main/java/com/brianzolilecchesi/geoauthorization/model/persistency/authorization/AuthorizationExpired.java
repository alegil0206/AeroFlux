package com.brianzolilecchesi.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.brianzolilecchesi.geoauthorization.util.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AuthorizationExpired extends Authorization {
	
	@Id
	private Long id;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime expiredAt;
	
	AuthorizationExpired(
			final Long id,
			final Drone drone,
			final Geozone geozone, 
			final Duration duration, 
			final LocalDateTime createdAt,
			final LocalDateTime expiredAt
			) {
		super(drone, geozone, duration, createdAt);
		setId(id);
		setExpiredAt(expiredAt);
	}

	AuthorizationExpired() {
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
	
	@Override
	public boolean isValid() {
		return false;
	}
	
	@Override
	public String getStatus() {
		return Constants.AUTHORIZATION_EXPIRED_STATUS;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationExpired[%s, id=%s, expiration=%s]",
				super.toString(), id, expiredAt
				);
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthorizationExpired that = (AuthorizationExpired) obj;
        return id != null && id.equals(that.id);
    }
}
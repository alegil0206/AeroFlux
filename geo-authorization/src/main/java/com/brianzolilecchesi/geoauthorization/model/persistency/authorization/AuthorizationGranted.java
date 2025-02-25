package com.brianzolilecchesi.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.brianzolilecchesi.geoauthorization.util.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AuthorizationGranted extends Authorization {
	
	@Id
	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	@Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;
	
	AuthorizationGranted(
			final Drone drone, 
			final Geozone geozone, 
			final Duration duration, 
			final LocalDateTime createdAt,
			final LocalDateTime expiresAt
			) {
        super(drone, geozone, duration, createdAt);
        setExpiresAt(expiresAt);
    }
	
	AuthorizationGranted() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
		
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public boolean isValid() {
		return 
				LocalDateTime.now().isAfter(getCreatedAt()) &&
				LocalDateTime.now().isBefore(expiresAt);
	}

	@Override
	public String getStatus() {
		return Constants.AUTHORIZATION_GRANTED_STATUS;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationDenied[%s, expiresAt=%s]",
				super.toString(),
				expiresAt
                );
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthorizationGranted that = (AuthorizationGranted) obj;
        return id != null && id.equals(that.id);
    }
}
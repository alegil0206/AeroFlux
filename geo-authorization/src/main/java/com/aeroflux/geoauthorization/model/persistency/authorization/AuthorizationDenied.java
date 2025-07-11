package com.aeroflux.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;
import com.aeroflux.geoauthorization.util.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AuthorizationDenied extends Authorization {
	
	@Id
	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	@Column(nullable = false, updatable = false)
	private String reason;
	
	AuthorizationDenied(
			final Drone drone, 
			final Geozone geozone, 
			final Duration duration, 
			final LocalDateTime createdAt,
			final String reason
			) {
        super(drone, geozone, duration, createdAt);
        setReason(reason);
    }
	
	AuthorizationDenied() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
        this.id = id;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public String getStatus() {
		return Constants.AUTHORIZATION_DENIED_STATUS;
	}
	
	@Override
	public String toString() {
		return String.format(
				"AuthorizationDenied[%s, reason=%s]",
				super.toString(),
				reason
                );
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthorizationDenied that = (AuthorizationDenied) obj;
        return id != null && id.equals(that.id);
    }
}
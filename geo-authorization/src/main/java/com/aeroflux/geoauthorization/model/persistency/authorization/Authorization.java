package com.aeroflux.geoauthorization.model.persistency.authorization;

import java.time.LocalDateTime;

import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;

@MappedSuperclass
@SequenceGenerator(name = "id_sequence", sequenceName = "shared_sequence", initialValue = 1, allocationSize = 1)
public abstract class Authorization {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drone_id", nullable = false, updatable = false)
    private Drone drone;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "geozone_id", nullable = false, updatable = false)
    private Geozone geozone;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "duration_name", referencedColumnName = "name", nullable = false, updatable = false)
    private Duration duration;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Authorization(final Drone drone, final Geozone geozone, final Duration duration, LocalDateTime createdAt) {
        this.drone = drone;
        this.geozone = geozone;
        this.duration = duration;
        this.createdAt = createdAt;
    }
    
	protected Authorization() {
	}
	
	public abstract Long getId();
	public abstract void setId(Long id);
    
    public Drone getDrone() {
        return drone;
    }

    public Geozone getGeozone() {
        return geozone;
    }
    
	public Duration getDuration() {
		return duration;
	}
	
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public abstract String getStatus();
    
    public abstract boolean isValid();

    @Override
    public String toString() {
        return String.format(
            "Authorization[drone=%s, geozone=%s, durationType=%s, durationValue=%s, createdAt=%s]",
            drone, geozone, duration, createdAt
        );
    }
}
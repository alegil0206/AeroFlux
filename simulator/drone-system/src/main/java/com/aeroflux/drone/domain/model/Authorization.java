package com.aeroflux.drone.domain.model;

import com.aeroflux.drone.domain.dto.AuthorizationDTO;

public class Authorization {
	
	public static enum Status {
		GRANTED("GRANTED"), DENIED("DENIED"), EXPIRED("EXPIRED"), REVOKED("REVOKED");

		private String status;

		private Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}
	
	public static enum DurationType {
		SHORT("SHORT"), STANDARD("STANDARD"), EXTENDED("EXTENDED");
		private String duration;
		
		private DurationType(String duration) {
			this.duration = duration;
		}
		
		public String getName() {
			return duration;
		}
	}
	
    private final String status;
    private final Long id;
    private final String droneId;
    private final String geozoneId;
    private final String durationType;
    private final Double duration;
    private final String startTime;
    private final String reason;
    private final String endTime;
    private final String revocationTime;

    public Authorization(
    		final Long id, 
    		final String droneId,
    		final String geozoneId, 
    		final String durationType, 
    		final Double duration, 
    		final String startTime, 
    		final String status, 
    		final String reason, 
    		final String endTime, 
    		final String revocationTime
            ) {
    	
    	assert id != null;
    	assert droneId != null;
    	assert geozoneId != null;
    	assert durationType != null;
    	assert duration != null;
    	assert startTime != null;
    	assert status != null;
    	
        this.id = id;
        this.droneId = droneId;
        this.geozoneId = geozoneId;
        this.durationType = durationType;
        this.duration = duration;
        this.startTime = startTime;
        this.status = status;
        this.reason = reason;
        this.endTime = endTime;
        this.revocationTime = revocationTime;
    }

    public Authorization(final AuthorizationDTO dto) {
		this(
				dto.getId(), 
				dto.getDroneId(), 
				dto.getGeozoneId(), 
				dto.getDurationType(), 
				dto.getDuration(),
				dto.getStartTime(), 
				dto.getStatus(), 
				dto.getReason(), 
				dto.getEndTime(), 
				dto.getRevocationTime()
				);
    }

    public Long getId() {
        return id;
    }

    public String getDroneId() {
        return droneId;
    }

    public String getGeozoneId() {
        return geozoneId;
    }

    public String getDurationType() {
        return durationType;
    }

    public Double getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStatus(){
        return status;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public String getRevocationTime() {
        return revocationTime;
    }
    
	public boolean isGranted() {
		return status.equalsIgnoreCase(Status.GRANTED.getStatus());
	}

	@Override
    public String toString() {
		return String.format("Authorization[id=%d, droneId=%s, geozoneId=%s, durationType=%s, duration=%f, startTime=%s, status=%s, reason=%s, endTime=%s, revocationTime=%s]",
				id, 
				droneId, 
				geozoneId, 
				durationType, 
				duration, 
				startTime, 
				status, 
				reason, 
				endTime, 
				revocationTime
				);
	}
	
    @Override
    public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Authorization that = (Authorization) o;

		return id.equals(that.id) &&
			   status.equals(that.status);
    }
    
    @Override
    public int hashCode() {
		return id.hashCode();
	}   
}
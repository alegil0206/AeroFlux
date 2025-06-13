package com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph;

public class FlightPlanCalculationReport {
	
	public enum Code {
		REPORT_CREATED(0, "Report created"),
		LINEAR_PATH_FOUND(1, "Linear path found"), 
		SOURCE_NOT_FOUND(2, "Source not found"),
		DESTINATION_NOT_FOUND(3, "Destination not found"),
		NO_PATH_FOUND(4, "No path found");
		
		private int code;
		private String message;
		
		Code(int code, String message) {
            this.code = code;
        }
		
		public int getCode() {
			return code;
		}
		
		public String getMessage() {
			return message;
		}
	}
	
	private Code code;
	private String algorithm;
	private double sensitivity;
	private double timeSeconds;
	
	public FlightPlanCalculationReport(
			final Code code, 
			final String algorithm,
			final double sensitivity,
			final double timeSeconds
			) {
		this.code = code;
		setAlgorithm(algorithm);
		setSensitivity(sensitivity);
		this.timeSeconds = timeSeconds;
	}
	
	public FlightPlanCalculationReport(
			final String algorithm,
			final double sensitivity
			) {
		this(Code.REPORT_CREATED, algorithm, sensitivity, 0);
	}
	
	public int getCode() {
		return code.code;
	}
	
	public String getMessage() {
		return code.message;
	}
	
	public void setCode(Code code) {
		this.code = code;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	public double getSensitivity() {
		return sensitivity;
	}
	
	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public double getTimeSeconds() {
		return timeSeconds;
	}
	
	public void setTimeSeconds(double timeSeconds) {
        this.timeSeconds = timeSeconds;
	}
	
	@Override
	public String toString() {
		return String.format("FlightPlanCalculationLog[code=%s, message=%s, algorithm=%s, sensitivity=%s, time=%s]",
				code.getMessage(),
				code.getCode(),
				getAlgorithm(),
				getSensitivity(), 
				getTimeSeconds()
				);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FlightPlanCalculationReport)) {
			return false;
		}
		FlightPlanCalculationReport other = (FlightPlanCalculationReport) o;
		return
				other.code.getCode() == code.getCode() &&
				other.code.getMessage().equals(code.getMessage()) &&
				other.getAlgorithm().equals(getAlgorithm()) &&
				other.getSensitivity() == getSensitivity() && 
				other.getTimeSeconds() == getTimeSeconds();
	}

	@Override
	public int hashCode() {
		int result = code.getCode();
		result = 31 * result + code.getMessage().hashCode();
		result = 31 * result + getAlgorithm().hashCode();
		result = 31 * result + Double.hashCode(getSensitivity());
		result = 31 * result + Double.hashCode(getTimeSeconds());
		return result;
	}
}
package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

public class FlightPlanCalculationReport {
	
	public enum Code {
		REPORT_CREATED(0, "Report created"),
		LINEAR_PATH_FOUND(1, "Linear path found"), 
		SOURCE_NOT_FOUND(2, "Source not found"),
		DESTINATION_NOT_FOUND(3, "Destination not found"),
		NO_PATH_FOUND(4, "No path found"),
		TARGET_SENSITIVITY_REACHED(5, "Target sensitivity reached"),
		ACCEPTABLE_SENSITIVITY_REACHED(7, "Acceptable sensitivity reached"),
		UNABLE_TO_IMPROVE_SENSITIVITY(8, "Unable to improve sensitivity"),
		MAX_TIME_REACHED(9, "Max time reached");
		
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
	private int iterations;
	
	public FlightPlanCalculationReport(
			final Code code, 
			final String algorithm,
			final double sensitivity,
			final double timeSeconds, 
			final int iterations
			) {
		this.code = code;
		setAlgorithm(algorithm);
		setSensitivity(sensitivity);
		this.timeSeconds = timeSeconds;
		this.iterations = iterations;
	}
	
	public FlightPlanCalculationReport(
			final String algorithm,
			final double sensitivity
			) {
		this(Code.REPORT_CREATED, algorithm, sensitivity, 0, 0);
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
	
	public int getIterations() {
		return iterations;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	@Override
	public String toString() {
		return String.format("FlightPlanCalculationLog[code=%s, message=%s, algorithm=%s, sensitivity=%s, time=%s, iterations=%s]",
				code.getMessage(),
				code.getCode(),
				getAlgorithm(),
				getSensitivity(), 
				getTimeSeconds(), 
				getIterations()
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
				other.getTimeSeconds() == getTimeSeconds() && 
				other.getIterations() == getIterations();
	}
}
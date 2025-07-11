package com.aeroflux.geoawareness.validation;

import java.util.Optional;

public class Range {
	
	public enum RangeExclude {
		LOWER, UPPER, BOTH, NONE
	}
	
	private final Optional<Double> min;
	private final Optional<Double> max;
	
	public Range(final Optional<Double> min, Optional<Double> max) {
		assert 
			min.isEmpty() || 
			max.isEmpty() ||
			min.isPresent() && max.isPresent() && min.get() <= max.get();
		
		this.min = min;
		this.max = max;
	}
	
	public Range(final Optional<Double> min, final double max) {
		this(min, Optional.of(max));
		
	}
	
	public Range(final double min, final Optional<Double> max) {
		this(Optional.of(min), max);
	}
	
	
	public Range(final double min, final double max) {
		this(Optional.of(min), Optional.of(max));
	}
	
	
	public boolean isWithinRange(final double value, RangeExclude rangeExclude) {
		if (min.isEmpty() && max.isEmpty()) {
            return true;
        }
				
		switch (rangeExclude) {
		case LOWER:
			if (min.isEmpty()) {
				return value <= max.get();
			} else if (max.isEmpty()) {
				return value > min.get();
			} else {
				return value > min.get() && value <= max.get();
			}
		case UPPER:
			if (min.isEmpty()) {
				return value < max.get();
			} else if (max.isEmpty()) {
				return value >= min.get();
			} else {
				return value >= min.get() && value < max.get();
			}
		case BOTH:
			if (min.isEmpty()) {
				return value < max.get();
			} else if (max.isEmpty()) {
				return value > min.get();
			} else {
				return value > min.get() && value < max.get();
			}
		case NONE:
			if (min.isEmpty()) {
				return value <= max.get();
			} else if (max.isEmpty()) {
				return value >= min.get();
			} else {
				return value >= min.get() && value <= max.get();
			}
		default:
			return false;
		}
	}

	public boolean isWithinRange(final double value) {
		return isWithinRange(value, RangeExclude.NONE);
	}

	public Double getMin() {
		return min.orElse(Double.MIN_VALUE);
	}

	public Double getMax() {
		return max.orElse(Double.MAX_VALUE);
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s]", 
				min.isEmpty() ? "-INF" : min.get(), 
				max.isEmpty() ? "+INF" : max.get()
				);
	}
}
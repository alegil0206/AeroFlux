package com.aeroflux.geoauthorization.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aeroflux.geoauthorization.dto.DurationDTO;
import com.aeroflux.geoauthorization.exception.ForbiddenException;
import com.aeroflux.geoauthorization.exception.NotFoundException;
import com.aeroflux.geoauthorization.model.persistency.authorization.Duration;
import com.aeroflux.geoauthorization.model.repository.DurationRepository;
import com.aeroflux.geoauthorization.util.Constants;

@Service
public class DurationService {
	
	private final DurationRepository repository;
	
	public DurationService(final DurationRepository repository) {
		this.repository = repository;
	}
		
	public List<DurationDTO> findAll() {
		List<DurationDTO> durationsDTO = new ArrayList<>();
		repository.findAll().forEach(duration -> durationsDTO.add(
				new DurationDTO(duration.getName(), duration.getTime(), Constants.DURATION_UNIT)
				));
		
		return durationsDTO;
	}
	
	Duration findStandardDuration() {
		return repository.findById(Constants.DURATION_STANDARD_NAME).orElseThrow(() -> new RuntimeException("Standard duration not found"));
	}
	
	Duration findDurationByValue(final double value) throws ForbiddenException {
		List<Duration> durations = repository.findAll();
		int i = 0;
		while (i < durations.size() && value > durations.get(i).getTime()) {
			++i;
		}
		if (i == durations.size()) {
			throw new ForbiddenException(String.format("Cannot release an authorization with a duration of %s %s", value, Constants.DURATION_UNIT));
		}
		
		return durations.get(i);
	}
	
	Duration findDurationByName(String name) throws NotFoundException {
		return repository.findById(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Duration not found: " + name));
	}
	
	public DurationDTO findByName(String name) throws NotFoundException {
		Duration Duration = findDurationByName(name);
		return new DurationDTO(Duration.getName(), Duration.getTime(), Constants.DURATION_UNIT);
	}
}
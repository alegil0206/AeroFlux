package com.aeroflux.geoawareness.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.dto.AltitudeDTO;
import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.model.persistency.Altitude;
import com.aeroflux.geoawareness.model.repository.AltitudeRepository;
import com.aeroflux.geoawareness.util.Constants;

@Service
public class AltitudeService {
	
	private final AltitudeRepository repository;
	
	public AltitudeService(final AltitudeRepository repository) {
		this.repository = repository;
	}
	
	public List<AltitudeDTO> findAll() {
		List<AltitudeDTO> altitudesDTO = new ArrayList<>();
		repository.findAll().forEach(altitude -> altitudesDTO.add(
				new AltitudeDTO(altitude.getName(), altitude.getValue(), Constants.ALTITUDE_UNIT)
				));
		
		return altitudesDTO;
	}
	
	Altitude findAltitudeByName(String name) throws NotFoundException {
		return repository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Altitude not found: " + name));
	}
	
	public AltitudeDTO findByName(String name) throws NotFoundException {
		Altitude altitude = findAltitudeByName(name);
		return new AltitudeDTO(altitude.getName(), altitude.getValue(), Constants.ALTITUDE_UNIT);
	}
}
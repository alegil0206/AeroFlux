package com.brianzolilecchesi.geoauthorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoauthorization.dto.PublishedGeozoneDTO;
import com.brianzolilecchesi.geoauthorization.exception.NotFoundException;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.GeozoneFactorySingleton;
import com.brianzolilecchesi.geoauthorization.model.repository.GeozoneRepository;

import jakarta.transaction.Transactional;

@Service
public class GeozoneService {
	
	private final GeozoneRepository repository;
	
	@Autowired
	public GeozoneService(final GeozoneRepository repository) {
		this.repository = repository;
	}
	
	Geozone getGeozoneById(final String id) throws NotFoundException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException("Geozone with id " + id + " not found"));
    }
    
	@Transactional
    public PublishedGeozoneDTO update(final PublishedGeozoneDTO geozoneDTO) throws NotFoundException {
    	Geozone geozone = getGeozoneById(geozoneDTO.getId());
    	geozone.update(geozoneDTO);
    	repository.save(geozone);
    	return geozoneDTO;
    }
    
	@Transactional
	public PublishedGeozoneDTO save(final PublishedGeozoneDTO geozoneDTO) {
		Geozone geozone = GeozoneFactorySingleton.getInstance().createGeozone(geozoneDTO);
		repository.save(geozone);
		return geozoneDTO;
	}
	
	@Transactional
    public void delete(final Geozone geozone) {
    	assert geozone != null;
        repository.delete(geozone);        
    }
   
	@Transactional
    public void deleteAll() {
		repository.deleteAll();
	}
}
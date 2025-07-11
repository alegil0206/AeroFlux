package com.aeroflux.geoawareness.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.GeozoneNotFoundException;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.exception.ForbiddenException;
import com.aeroflux.geoawareness.model.persistency.Type;
import com.aeroflux.geoawareness.model.persistency.geozone.Geozone;
import com.aeroflux.geoawareness.model.persistency.geozone.GeozoneFactory;
import com.aeroflux.geoawareness.model.persistency.geozone.GeozoneUpdater;
import com.aeroflux.geoawareness.model.repository.GeozoneRepository;
import com.aeroflux.geoawareness.validation.GeozoneValidatorFacadeSingleton;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeozoneService {

    private final GeozoneRepository repository;
    private final GeozoneFactory geozoneFactory;
    private final GeozoneUpdater geozoneUpdater;

    @Autowired
    public GeozoneService(
    		final GeozoneRepository repository, 
    		final GeozoneFactory geozoneFactory,
    		final GeozoneUpdater geozoneUpdater
    		) {
    	
        this.repository = repository;
        this.geozoneFactory = geozoneFactory;
        this.geozoneUpdater = geozoneUpdater;
    }
    
    private Geozone getGeozoneById(final String id) throws GeozoneNotFoundException {
    	return repository.findById(id).orElseThrow(() -> new GeozoneNotFoundException());
    }
    
    public List<GeozoneDTO> getAll() {
    	List<GeozoneDTO> geozonesDTO = new ArrayList<>();
    	
		repository.findAll().forEach(geozone -> {
			geozonesDTO.add(geozoneFactory.createGeozoneDTO(geozone));
		});
  
    	return geozonesDTO;
    }   
    
    public GeozoneDTO getById(final String id) throws GeozoneNotFoundException {
    	return geozoneFactory.createGeozoneDTO(getGeozoneById(id));    	
    }
     
	public GeozoneDTO save(final GeozoneDTO geozoneDTO) throws IllegalGeozoneException {
		GeozoneValidatorFacadeSingleton.getInstance().validate(geozoneDTO);
		Geozone geozone = geozoneFactory.createGeozone(geozoneDTO);
		geozone = repository.save(geozone);
		return geozoneFactory.createGeozoneDTO(geozone);
	}
	
	public GeozoneDTO update(final GeozoneDTO geozoneDTO) throws ForbiddenException, IllegalGeozoneException, GeozoneNotFoundException {
    	GeozoneValidatorFacadeSingleton.getInstance().validate(geozoneDTO);
    	Geozone geozone = getGeozoneById(geozoneDTO.getId());
    	geozoneUpdater.update(geozone, geozoneDTO);
    	repository.save(geozone);
    	
    	return geozoneFactory.createGeozoneDTO(geozone);
    }

    public GeozoneDTO deleteById(final String id) throws GeozoneNotFoundException {
    	GeozoneDTO geozoneDTO = getById(id);
        repository.deleteById(id);
        
        return geozoneDTO;
    }
    
	public void deleteAll() {
		repository.deleteAll();
	}
	
	GeozoneDTO getExampleGeozoneDTO(final Type type) {
		return geozoneFactory.createExampleGeozoneDTO(type);
	}
}
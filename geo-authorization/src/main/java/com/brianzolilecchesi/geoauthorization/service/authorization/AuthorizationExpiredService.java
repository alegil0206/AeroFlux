package com.brianzolilecchesi.geoauthorization.service.authorization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoauthorization.exception.authorization.AuthorizationNotFoundException;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.Authorization;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationExpired;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.brianzolilecchesi.geoauthorization.model.repository.AuthorizationExpiredRepository;

@Service
public class AuthorizationExpiredService implements AuthorizationServiceInterface {

	private final AuthorizationExpiredRepository repository;

	@Autowired
	AuthorizationExpiredService(final AuthorizationExpiredRepository repository) {
		this.repository = repository;
	}

	@Override
	public Authorization getById(final Long id) throws AuthorizationNotFoundException {
		return repository.findById(id).orElseThrow(() -> new AuthorizationNotFoundException(id));		
	}
	
	@Override
	public List<Authorization> getByDroneAndGeozone(final Drone drone, final Geozone geozone) {
		assert drone != null;
		assert geozone != null;
		
		return new ArrayList<>(repository.findByDroneAndGeozone(drone, geozone));
	}
	
	@Override
	public List<Authorization> getAll() {
		return new ArrayList<>(repository.findAll());
	}
	
	@Override
	public List<Authorization> getByDrone(final Drone drone) {
		assert drone != null;
		return new ArrayList<>(repository.findByDrone(drone));
	}
	
	@Override
	public List<Authorization> getByGeozone(final Geozone geozone) {
		assert geozone != null;
		return new ArrayList<>(repository.findByGeozone(geozone));
	}
	
	@Override
	public AuthorizationExpired save(final Authorization authorization) {
		assert authorization != null;
		assert authorization instanceof AuthorizationExpired;
		
		return save((AuthorizationExpired) authorization);
	}
	
	private AuthorizationExpired save(final AuthorizationExpired authorization) {
		assert authorization != null;
		return repository.save(authorization);
	}
	
	@Override
	public void deleteById(final Long id) {
		repository.deleteById(id);
	}
	
	@Override
	public void deleteByDrone(final Drone drone) {
        assert drone != null;
        repository.deleteByDrone(drone);
	}
	
	@Override
	public void deleteByGeozone(final Geozone geozone) {
		assert geozone != null;
		repository.deleteByGeozone(geozone);
	}
	
	@Override
	public void deleteAll() {
		repository.deleteAll();
	}
}
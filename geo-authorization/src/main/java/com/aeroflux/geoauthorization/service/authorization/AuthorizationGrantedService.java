package com.aeroflux.geoauthorization.service.authorization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aeroflux.geoauthorization.exception.authorization.AuthorizationNotFoundException;
import com.aeroflux.geoauthorization.model.persistency.authorization.Authorization;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationGranted;
import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;
import com.aeroflux.geoauthorization.model.repository.AuthorizationGrantedRepository;

@Service
public class AuthorizationGrantedService implements AuthorizationServiceInterface {

	private final AuthorizationGrantedRepository repository;

	@Autowired
	AuthorizationGrantedService(final AuthorizationGrantedRepository repository) {
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
	public AuthorizationGranted save(final Authorization authorization) {
		assert authorization != null;
		assert authorization instanceof AuthorizationGranted;
		
		return save((AuthorizationGranted) authorization);
	}
	
	private AuthorizationGranted save(final AuthorizationGranted authorization) {
		assert authorization != null;
		return repository.save(authorization);
	}
	
	public void delete(final AuthorizationGranted authorization) {
		assert authorization != null;
		repository.delete(authorization);
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
	
	public List<AuthorizationGranted> getAuthorizationsExpired(final LocalDateTime expiresAt) {
		assert expiresAt != null;
		return repository.findByExpiresAtBefore(expiresAt);
	}
	
	public List<AuthorizationGranted> getAuthorizationsExpired() {
		return repository.findByExpiresAtBefore(LocalDateTime.now());
	}
}
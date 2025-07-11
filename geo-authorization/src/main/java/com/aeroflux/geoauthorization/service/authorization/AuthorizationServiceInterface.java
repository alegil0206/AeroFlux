package com.aeroflux.geoauthorization.service.authorization;

import java.util.List;

import com.aeroflux.geoauthorization.exception.authorization.AuthorizationNotFoundException;
import com.aeroflux.geoauthorization.model.persistency.authorization.Authorization;
import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;

public interface AuthorizationServiceInterface {
	
	Authorization getById(Long id) throws AuthorizationNotFoundException;
	
	List<Authorization> getAll();
	List<Authorization> getByDroneAndGeozone(final Drone drone, final Geozone geozone) throws AuthorizationNotFoundException;
	List<Authorization> getByDrone(final Drone drone) throws AuthorizationNotFoundException;
	List<Authorization> getByGeozone(final Geozone geozone) throws AuthorizationNotFoundException;
	
	Authorization save(final Authorization authorization);
	
	void deleteById(final Long id);
	void deleteByDrone(final Drone drone);
	void deleteByGeozone(final Geozone geozone);
	void deleteAll();
}
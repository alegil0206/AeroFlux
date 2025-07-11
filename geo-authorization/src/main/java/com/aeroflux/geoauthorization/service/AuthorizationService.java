package com.aeroflux.geoauthorization.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aeroflux.geoauthorization.model.persistency.authorization.Duration;
import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;
import com.aeroflux.geoauthorization.service.authorization.AuthorizationDeniedService;
import com.aeroflux.geoauthorization.service.authorization.AuthorizationExpiredService;
import com.aeroflux.geoauthorization.service.authorization.AuthorizationGrantedService;
import com.aeroflux.geoauthorization.service.authorization.AuthorizationRevokedService;
import com.aeroflux.geoauthorization.service.authorization.AuthorizationServiceInterface;
import com.aeroflux.geoauthorization.dto.AuthorizationDTO;
import com.aeroflux.geoauthorization.dto.AuthorizationRequestDTO;
import com.aeroflux.geoauthorization.exception.BadRequestException;
import com.aeroflux.geoauthorization.exception.ForbiddenException;
import com.aeroflux.geoauthorization.exception.NotFoundException;
import com.aeroflux.geoauthorization.exception.authorization.AuthorizationNotFoundException;
import com.aeroflux.geoauthorization.exception.authorization.badrequest.IllegalAuthorizationRequestDurationException;
import com.aeroflux.geoauthorization.exception.authorization.forbidden.ActiveAuthorizationException;
import com.aeroflux.geoauthorization.model.authorizator.AuthorizatorSingleton;
import com.aeroflux.geoauthorization.model.persistency.authorization.Authorization;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationGranted;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationRevoked;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationDenied;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationFactory;
import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationFactorySingleton;

import jakarta.transaction.Transactional;

@Service
public class AuthorizationService {
	
	private final AuthorizationGrantedService authorizationGrantedService;
	private final AuthorizationDeniedService authorizationDeniedService;
	private final AuthorizationRevokedService authorizationRevokedService;
	private List<AuthorizationServiceInterface> authorizationServices;
	
	private final GeozoneService geozoneService;
	private final DroneService droneService;
	private final DurationService durationService;
	
	@Autowired
	public AuthorizationService(
			final AuthorizationGrantedService authorizationGrantedService,
			final AuthorizationDeniedService authorizationDeniedService,
			final AuthorizationExpiredService authorizationExpiredService,
			final AuthorizationRevokedService authorizationRevokedService,
			final GeozoneService geozoneService,
			final DroneService droneService,
			final DurationService durationService
			) {
		this.authorizationGrantedService = authorizationGrantedService;
		this.authorizationDeniedService = authorizationDeniedService;
		this.authorizationRevokedService = authorizationRevokedService;
		this.authorizationServices = List.of(
				this.authorizationGrantedService,
				this.authorizationDeniedService,
				authorizationExpiredService,
				this.authorizationRevokedService
				);
		
		this.geozoneService = geozoneService;
		this.droneService = droneService;
		this.durationService = durationService;
	}
	
	private Authorization getAuthorizationById(final Long id) throws AuthorizationNotFoundException {
		Authorization authorization = null;
		for (AuthorizationServiceInterface service : authorizationServices) {
			try {
                authorization = service.getById(id);
                break;
			} catch (AuthorizationNotFoundException e) {
				continue;
			}
		}
		
		if (authorization != null) {
			return authorization;
		}
        throw new AuthorizationNotFoundException(id);
	}
	
	public List<AuthorizationDTO> getByDroneAndGeozoneId(final String droneId, final String geozoneId) throws NotFoundException {
		Drone drone = droneService.getDroneById(droneId);
		Geozone geozone = geozoneService.getGeozoneById(geozoneId);
		AuthorizationFactory factory = AuthorizationFactorySingleton.getInstance();
		
		List<AuthorizationDTO> authorizations = new ArrayList<>();
		for (AuthorizationServiceInterface service : authorizationServices) {
            service.getByDroneAndGeozone(drone, geozone).forEach(authorization -> {
				authorizations.add(factory.createAuthorizationDTO(authorization));
			});
        }
		
		return authorizations;
	}
	
	public List<AuthorizationDTO> getAll() {
		List<AuthorizationDTO> authorizationsDTO = new ArrayList<>();
		AuthorizationFactory factory = AuthorizationFactorySingleton.getInstance();
		
		for (AuthorizationServiceInterface service : authorizationServices) {
			service.getAll().forEach(authorization -> {
				authorizationsDTO.add(factory.createAuthorizationDTO(authorization)
						);
			});
		}
        
        return authorizationsDTO;
	}
	
	public AuthorizationDTO getById(final Long id) throws AuthorizationNotFoundException {
		return AuthorizationFactorySingleton.getInstance().createAuthorizationDTO(getAuthorizationById(id));
	}
	
	public List<AuthorizationDTO> getByDroneId(final String droneId) throws NotFoundException {
		Drone drone = droneService.getDroneById(droneId);
		List<AuthorizationDTO> authorizationsDTO = new ArrayList<>();
		AuthorizationFactory factory = AuthorizationFactorySingleton.getInstance();
		
		for (AuthorizationServiceInterface service : authorizationServices) {
			service.getByDrone(drone).forEach(authorization -> {
				authorizationsDTO.add(factory.createAuthorizationDTO(authorization));
			});
		}
		
		return authorizationsDTO;
	}
	
	public List<AuthorizationDTO> getByGeozoneId(final String geozoneId) throws NotFoundException {
		Geozone geozone = geozoneService.getGeozoneById(geozoneId);
		List<AuthorizationDTO> authorizationsDTO = new ArrayList<>();
		AuthorizationFactory factory = AuthorizationFactorySingleton.getInstance();

		for (AuthorizationServiceInterface service : authorizationServices) {
			service.getByGeozone(geozone).forEach(authorization -> {
				authorizationsDTO.add(factory.createAuthorizationDTO(authorization));
			});
		}

		return authorizationsDTO;
	}
	
	@Transactional
	public AuthorizationDTO requestAuthorization(final AuthorizationRequestDTO authorizationRequestDTO)
			throws NotFoundException, BadRequestException, ForbiddenException {
		assert authorizationRequestDTO != null;		
		
		Geozone geozone = geozoneService.getGeozoneById(authorizationRequestDTO.getGeozoneId());
		Drone drone = droneService.getDroneById(authorizationRequestDTO.getDroneId());
		
		List<Authorization> authorizations = authorizationGrantedService.getByDroneAndGeozone(drone, geozone);
		for (Authorization authorization : authorizations) {
            if (authorization.isValid()) {
                throw new ActiveAuthorizationException(
                		authorization.getId(), 
                		drone.getId(), 
                		geozone.getId()
                		);
            }
		}
		
		Duration duration = null;
		if (authorizationRequestDTO.getDuration() == null) {
			duration = durationService.findStandardDuration();
		} else {
			try {
				duration = durationService.findDurationByName(authorizationRequestDTO.getDuration().toUpperCase());
			} catch (NotFoundException e) {
				Double durationValue = null;
				try {
					durationValue = Double.parseDouble(authorizationRequestDTO.getDuration());
				} catch (NumberFormatException e2) {
					throw new IllegalAuthorizationRequestDurationException(authorizationRequestDTO.getDuration());
				}
				duration = durationService.findDurationByValue(durationValue);
			}
		}
		Authorization authorization = AuthorizatorSingleton
				.getInstance()
				.authorize(drone, geozone, duration); 
		
		if (authorization instanceof AuthorizationGranted) {
			authorization = authorizationGrantedService.save(authorization);
		} else if (authorization instanceof AuthorizationDenied)  {
			authorization = authorizationDeniedService.save(authorization);
		} else {
			throw new RuntimeException("Authorization type not recognized: this should never happen!");
		}
		return AuthorizationFactorySingleton.getInstance().createAuthorizationDTO(authorization);
	}
	
	@Transactional
	public AuthorizationDTO revokeAuthorization(final Long id) throws AuthorizationNotFoundException, ForbiddenException {
		Authorization authorization = authorizationGrantedService.getById(id);
		if (!(authorization instanceof AuthorizationGranted)) {
			throw new RuntimeException("Attempt to revoke a not granted authorization: this should never happen!");
		}
		
		AuthorizationGranted granted = (AuthorizationGranted) authorization;
		if (!granted.isValid()) {
			throw new ForbiddenException("Attempt to revoke an expired authorization");
		}
		
		AuthorizationFactory factory = AuthorizationFactorySingleton.getInstance();		
		AuthorizationRevoked revoked = factory.createAuthorizationRevoked(granted);
		revoked = authorizationRevokedService.save(revoked);
		authorizationGrantedService.delete(granted);
		
		return factory.createAuthorizationDTO(revoked);
	}

	@Transactional
	void deleteByDrone(final Drone drone) {
        assert drone != null;
		for (AuthorizationServiceInterface service : authorizationServices) {
			service.deleteByDrone(drone);
		}
	}
	
	@Transactional
	void deleteByGeozone(final Geozone geozone) {
		assert geozone != null;
		for (AuthorizationServiceInterface service : authorizationServices) {
			service.deleteByGeozone(geozone);
		}
	}
	
	@Transactional
	public void deleteAll() {
		for (AuthorizationServiceInterface service : authorizationServices) {
            service.deleteAll();
		}		
	}

	@Transactional
	public void updateAuthorizationsOnGeozoneChanges(String id) throws NotFoundException, ForbiddenException {
		Geozone geozone = geozoneService.getGeozoneById(id);
		if (!geozone.getCategory().equals("EXCLUDED")) return; 
		List<Authorization> authorizations = authorizationGrantedService.getByGeozone(geozone);
		for (Authorization authorization : authorizations) {
			if (authorization.isValid()) {
				revokeAuthorization(authorization.getId());
			}
		}
	}

}
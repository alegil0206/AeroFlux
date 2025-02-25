package com.brianzolilecchesi.geoauthorization.service.dbupdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationFactorySingleton;
import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationGranted;
import com.brianzolilecchesi.geoauthorization.service.authorization.AuthorizationExpiredService;
import com.brianzolilecchesi.geoauthorization.service.authorization.AuthorizationGrantedService;
import com.brianzolilecchesi.geoauthorization.util.Constants;

import jakarta.transaction.Transactional;

@Service
public class AuthorizationUpdater {

	private final AuthorizationGrantedService authorizationGrantedService;
	private final AuthorizationExpiredService authorizationExpiredService;
	
	@Autowired
	public AuthorizationUpdater(
			final AuthorizationGrantedService authorizationGrantedService,
			final AuthorizationExpiredService authorizationExpiredService
			) {
		
		this.authorizationGrantedService = authorizationGrantedService;
		this.authorizationExpiredService = authorizationExpiredService;
	}

    @Scheduled(fixedRate = Constants.AUTHORIZATION_UPDATE_INTERVAL_NANOSECONDS)
    @Transactional
    public void updateAuthorizations() {
    	List<AuthorizationGranted> granted = authorizationGrantedService.getAuthorizationsExpired();
    	
		for (AuthorizationGranted g : granted) {
			authorizationExpiredService.save(AuthorizationFactorySingleton
					.getInstance()
					.createAuthorizationExpired(g));
			
			authorizationGrantedService.delete(g);
		}
    }   
}
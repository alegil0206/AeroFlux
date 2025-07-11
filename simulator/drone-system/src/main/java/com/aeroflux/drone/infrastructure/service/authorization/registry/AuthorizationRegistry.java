package com.aeroflux.drone.infrastructure.service.authorization.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aeroflux.drone.domain.model.Authorization;

public class AuthorizationRegistry {
	
	private Map<String, Authorization> authorizations;
	
	private static Map<String, Authorization> fromList(List<Authorization> authorizations) {
		assert authorizations != null;
		
		Map<String, Authorization> map = new HashMap<>();
		for (Authorization authorization : authorizations) {
			map.put(authorization.getGeozoneId(), authorization);
		}
		
		return map;
	}
	
	public AuthorizationRegistry(Map<String, Authorization> authorizations) {
		assert authorizations != null;
		
		this.authorizations = authorizations;
	}
	
	public AuthorizationRegistry(List<Authorization> authorizations) {
		this(fromList(authorizations));
	}
	
	public AuthorizationRegistry() {
		this(new HashMap<>());
	}
	
	public Authorization get(String geozoneId) {
		return authorizations.get(geozoneId);
	}
	
	public Map<String, Authorization> getAll() {
		return authorizations;
	}
	
	public void set(Map<String, Authorization> authorizations) {
		assert authorizations != null;
		
		this.authorizations = authorizations;
	}
	
	public Authorization add(final Authorization authorization) {
		assert authorization != null;
		
		return this.authorizations.put(authorization.getGeozoneId(), authorization);
	}
	
	public void addAll(final List<Authorization> authorizations) {
		assert authorizations != null;
		
		for (Authorization authorization : authorizations) {
			assert authorization != null;
			this.authorizations.put(authorization.getGeozoneId(), authorization);
		}
	}
	
	public Authorization remove(final Authorization authorization) {
		return this.authorizations.remove(authorization.getGeozoneId());
	}
	
	public void clear() {
		this.authorizations.clear();
	}
	
	@Override
	public String toString() {
		return String.format("AuthorizationRegistry[authorizations=%s]", authorizations);
	}

}

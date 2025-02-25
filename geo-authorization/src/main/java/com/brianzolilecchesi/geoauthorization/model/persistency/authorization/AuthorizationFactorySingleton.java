package com.brianzolilecchesi.geoauthorization.model.persistency.authorization;

public class AuthorizationFactorySingleton {
	
	private static AuthorizationFactory instance;
	
	private AuthorizationFactorySingleton() {
	}
	
	public static AuthorizationFactory getInstance() {
		if (instance == null) {
			instance = new AuthorizationFactory();
		}
		return instance;
	}	
}
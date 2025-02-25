package com.brianzolilecchesi.geoauthorization.model.authorizator;

public class AuthorizatorSingleton {
	
	private static double SPECIFIC_DEFAULT_PROBABILITY = 0.5;
	private static double CERTIFIED_DEFAULT_PROBABILITY = 0.8;
	
	private static Authorizator instance;

	private AuthorizatorSingleton() {
	}

	public static Authorizator getInstance() {
		if (instance == null) {
			instance = new Authorizator(
					SPECIFIC_DEFAULT_PROBABILITY, 
					CERTIFIED_DEFAULT_PROBABILITY
					);
		}
		return instance;
	}	
}
package com.brianzolilecchesi.geoawareness.registry;

public class RegistryFacadeSingleton {
	
	private static RegistryFacade instance;

	private RegistryFacadeSingleton() {
	}

	public static RegistryFacade getInstance() {
		if (instance == null) {
			instance = new RegistryFacade();
		}
		return instance;
	}
}
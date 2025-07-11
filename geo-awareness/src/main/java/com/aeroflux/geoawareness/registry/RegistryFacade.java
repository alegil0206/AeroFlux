package com.aeroflux.geoawareness.registry;

public class RegistryFacade {
	
	private RegistryMap<Integer> altitudeRegistry;
	private RegistrySet categoryRegistry;
	private RegistrySet statusRegistry;
	private RegistrySet typeRegistry;
	
	RegistryFacade() {
		
		altitudeRegistry = new RegistryMap<>();
		categoryRegistry = new RegistrySet();
		statusRegistry = new RegistrySet();
		typeRegistry = new RegistrySet();
	}
	
	public RegistryMap<Integer> getAltitudeRegistry() {
		return altitudeRegistry;
	}
	
	public RegistrySet getCategoryRegistry() {
		return categoryRegistry;
	}
		
	public RegistrySet getStatusRegistry() {
		return statusRegistry;
	}
	
	public RegistrySet getTypeRegistry() {
		return typeRegistry;
	}
}
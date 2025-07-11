package com.aeroflux.geoawareness.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegistryMap<T> {
	
	private final Map<String, T> items; 
        
	RegistryMap() {
    	this.items = new HashMap<>();
	}
    
    public void register(String key, T value) {
        items.put(key, value);
    }

    public boolean contains(String key) {
    	return items.containsKey(key);
    }
    
	public T get(String key) {
		return items.get(key);
	}
	
	public String getKey(T value) {
		for (Map.Entry<String, T> entry : items.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
    
	public void unregister(String item) {
		items.remove(item);
	}
	
	public Set<String> getKeySet() {
		return new HashSet<>(items.keySet());
	}
	
	public List<T> getValues() {
		return new ArrayList<>(items.values());
	}
}
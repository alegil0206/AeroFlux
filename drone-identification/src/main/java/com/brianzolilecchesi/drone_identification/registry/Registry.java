package com.brianzolilecchesi.drone_identification.registry;

import java.util.HashSet;
import java.util.Set;

public class Registry {
	
	private final Set<String> items; 
        
	Registry() {
    	this.items = new HashSet<>();
	}
    
    public void register(String item) {
        items.add(item);
    }

    public boolean contains(String key) {
    	return items.contains(key);
    }
    
	public void unregister(String item) {
		items.remove(item);
	}
	
	public Set<String> getItems() {
		return new HashSet<>(items);
	}
}
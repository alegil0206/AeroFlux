package com.brianzolilecchesi.drone;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DroneSystem {

    private final String droneId;
    private String message;
    private PropertyChangeSupport support;
	private int i = 0;


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    
    public DroneSystem(String droneId) {
        this.droneId = droneId;
        this.support = new PropertyChangeSupport(this);
    }

    public void executeStep() {
        // Monitoraggio
        // Analisi
        // Pianificazione
        // Esecuzione
        message = "Drone " + droneId + " sta eseguendo il ciclo " + i + " MAPE-K";
		i++;
        System.out.println(message);
		if (i%10 == 0) {
        	support.firePropertyChange("message", null, message);
		}
    }
} 

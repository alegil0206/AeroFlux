package com.brianzolilecchesi.geoauthorization.model.persistency.authorization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Duration {

	@Id
	private String name;
	
	@Column(nullable = false)
	private int time;
	
	public Duration(String name, int value) {
		setName(name);
		setTime(value);
	}
	
	Duration() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int value) {
		this.time = value;
	}
	
	@Override
	public String toString() {
		return String.format("Duration[name=%s, time=%d]", name, time);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Duration other = (Duration) obj;
		return name.equals(other.name);
	}	
}
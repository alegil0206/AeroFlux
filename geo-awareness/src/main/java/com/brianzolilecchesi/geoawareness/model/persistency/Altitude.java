package com.brianzolilecchesi.geoawareness.model.persistency;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.brianzolilecchesi.geoawareness.util.Constants;

@Document(collection = Constants.ALTITUDE_COLLECTION_NAME)
public class Altitude {

    @Field("name")
    private String name;

    @Field("value")
    private Integer value;

    public Altitude(String name, Integer value) {
        setName(name);
        setValue(value);
    }
    
    public Altitude(String name, Double value) {
        this(name, value.intValue());
    }
    
	public Altitude() {
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
    	return String.format("Altitude[name=%s, value=%s]", name, value);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Altitude other = (Altitude) obj;
        return Objects.equals(name, other.name);
    }
}
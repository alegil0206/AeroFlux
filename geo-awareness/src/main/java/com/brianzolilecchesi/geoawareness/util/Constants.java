package com.brianzolilecchesi.geoawareness.util;

import java.util.Optional;

import com.brianzolilecchesi.geoawareness.validation.Range;

public class Constants {
	
	private Constants() {
	}
	
	public static final String TYPE_COLLECTION_NAME = "type";
	public static final String CATEGORY_COLLECTION_NAME = "category";
	public static final String ALTITUDE_COLLECTION_NAME = "altitude";
	public static final String STATUS_COLLECTION_NAME = "status";
	public static final String GEOZONE_COLLECTION_NAME = "geozone";
	public static final String SUPPORT_POINT_COLLECTION_NAME = "support_point";
	
	public static final Range ALTITUDE_RANGE = new Range(0, 120);
	public static final Range LONGITUDE_RANGE = new Range(-180.0, 180.0);
	public static final Range LATITUDE_RANGE = new Range(-90.0, 90.0);
	
	// Cannot be an enumeration to be used in annotations
	public static final String CIRCULAR_GEOZONE_TYPE = "CIRCULAR";
	public static final Range RADIUS_RANGE = new Range(0, Optional.empty());
	
	// Cannot be an enumeration to be used in annotations
	public static final String POLYGONAL_GEOZONE_TYPE = "POLYGONAL";
	
	public static final String ALTITUDE_UNIT = "METERS";
}
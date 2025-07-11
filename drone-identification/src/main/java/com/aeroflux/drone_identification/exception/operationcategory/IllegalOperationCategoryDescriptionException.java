package com.aeroflux.drone_identification.exception.operationcategory;


public class IllegalOperationCategoryDescriptionException extends IllegalOperationCategoryException {
	
	private static final long serialVersionUID = 1L;
		
	public IllegalOperationCategoryDescriptionException() {
		super("description not provided");
	}
}
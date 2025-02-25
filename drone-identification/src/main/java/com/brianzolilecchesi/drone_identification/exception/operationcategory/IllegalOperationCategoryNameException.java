package com.brianzolilecchesi.drone_identification.exception.operationcategory;

public class IllegalOperationCategoryNameException extends IllegalOperationCategoryException {
	
	private static final long serialVersionUID = 1L;
		
	public IllegalOperationCategoryNameException() {
		super("name not provided");
	}
	
	public IllegalOperationCategoryNameException(String name) {
		super(String.format(
				"operation category with name %s already present",
				name
				));
	}
}
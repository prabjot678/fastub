package com.fastub.api.exception;

public class DuplicateDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public DuplicateDataException(String message) {
		super(message);
		this.message = message;
	}
	
	

}

package com.fastub.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
	
	private String message;
	
	

	public EntityNotFoundException(String message) {
		super();
		this.message = message;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

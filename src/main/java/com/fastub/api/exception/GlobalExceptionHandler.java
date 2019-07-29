package com.fastub.api.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
		
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(),
		        "Not Found");
		    return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(DuplicateDataException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDuplicateDataException(DuplicateDataException ex) {
		
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(),
		        "Entity already exist.");
		    return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
    }

}

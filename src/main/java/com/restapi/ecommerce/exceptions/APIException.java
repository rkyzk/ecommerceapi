package com.restapi.ecommerce.exceptions;

/**
 * APIException class
 */
public class APIException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	 
	public APIException() {
	}
	 
	public APIException(String message) {
		super(message);
	}
}

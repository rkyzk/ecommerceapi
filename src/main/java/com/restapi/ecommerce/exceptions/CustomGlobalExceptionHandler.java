package com.restapi.ecommerce.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.restapi.ecommerce.payload.APIResponse;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		Map<String, String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(err -> {
			String fieldName = ((FieldError)err).getField();
			String message = err.getDefaultMessage();
			response.put(fieldName, message);
		});
	    return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		String message = e.getMessage();
		APIResponse apiResponse = new APIResponse(message , false);
		return new ResponseEntity<> (apiResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIResponse> handleAPIException(APIException e) {
		String message = e.getMessage();
		APIResponse apiResponse = new APIResponse(message , false);
		return new ResponseEntity<> (apiResponse, HttpStatus.BAD_REQUEST);
	}
}

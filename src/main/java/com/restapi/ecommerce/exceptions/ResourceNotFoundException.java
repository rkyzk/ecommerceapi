package com.restapi.ecommerce.exceptions;

/**
 * Exception to be thrown when resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
	String resourceName;
	String field;
	String strValue;
	Long longValue;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String resourceName, String field, String strValue) {
		super(String.format("Resource with the specified field value was not found:"
				+ "Resource: %s, Field: %s, Value: %s", resourceName, field, strValue));
		this.resourceName = resourceName;
		this.field = field;
		this.strValue = strValue;
	}

	public ResourceNotFoundException(String resourceName, String field, Long longValue) {
		super(String.format("Resource with the specified field value was not found:"
				+ "Resource: %s, Field: %s, Value: %d", resourceName, field, longValue));
		this.resourceName = resourceName;
		this.field = field;
		this.longValue = longValue;
	}
}

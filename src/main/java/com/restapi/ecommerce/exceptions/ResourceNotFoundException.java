package com.restapi.ecommerce.exceptions;

/**
 * リソース不在の例外クラス
 */
public class ResourceNotFoundException extends RuntimeException {
	String resourceName;
	String field;
	String fieldName;
	Long fieldId;
	
	public ResourceNotFoundException() {
	}
	
	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%sが%sの%sは見つかりません。", fieldName, field, resourceName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}
	
	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%sが%dの%sは見つかりません。", field, fieldId, resourceName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
}

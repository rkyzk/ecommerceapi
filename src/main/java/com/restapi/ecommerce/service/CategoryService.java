package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.CategoryDTO;
import com.restapi.ecommerce.payload.CategoryResponse;

public interface CategoryService {
	CategoryResponse getCategories(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public CategoryDTO createCategory(CategoryDTO categoryDTO);
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
	public CategoryDTO deleteCategory(Long categoryId);
}

package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.CategoryDTO;
import com.restapi.ecommerce.payload.CategoryResponse;

public interface CategoryService {
	CategoryResponse getCategories(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
	void deleteCategory(Long categoryId);
}

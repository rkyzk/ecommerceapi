package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.CategoryDTO;
import com.restapi.ecommerce.payload.CategoryResponse;

public interface CategoryService {
	public CategoryResponse getCategories(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public CategoryDTO createCategory(CategoryDTO categoryDTO);
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
	public void deleteCategory(Long categoryId);
}

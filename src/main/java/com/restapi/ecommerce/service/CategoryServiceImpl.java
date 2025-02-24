package com.restapi.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Category;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.CategoryDTO;
import com.restapi.ecommerce.payload.CategoryResponse;
import com.restapi.ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getCategories(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
		List<Category> categories = categoryPage.getContent();
		if (categories.isEmpty()) {
			throw new APIException("No categories present");
		}
		List<CategoryDTO> categoryDTOs = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class))
				.toList();
		CategoryResponse response = new CategoryResponse();
		response.setContent(categoryDTOs);
		// set pagination data
		response.setPageNumber(categoryPage.getNumber());
		response.setPageSize(categoryPage.getSize());
		response.setTotalElements(categoryPage.getTotalElements());
		response.setTotalPages(categoryPage.getTotalPages());
		response.setLastPage(categoryPage.isLast());
		return response;
	};

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
		Optional<Category> storedCategory = categoryRepository.findById(categoryId);
		Category categoryToUpdate = storedCategory
				.orElseThrow(()
						-> new ResourceNotFoundException("Category", "categoryId", categoryId));
	    categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());
	    Category updatedCategory = categoryRepository.save(categoryToUpdate);
	    CategoryDTO updatedProdDTO = modelMapper.map(updatedCategory, CategoryDTO.class);
		return updatedProdDTO;
	}
	
	@Override
	public void deleteCategory(Long categoryId) {
		Optional<Category> storedCategory = categoryRepository.findById(categoryId);	
		Category categoryToDelete = storedCategory
				.orElseThrow(()
						-> new ResourceNotFoundException("Category", "categoryId", categoryId));
	    categoryRepository.delete(categoryToDelete);
	}
}
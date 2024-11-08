package com.restapi.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.config.AppConstants;
import com.restapi.ecommerce.payload.CategoryDTO;
import com.restapi.ecommerce.payload.CategoryResponse;
import com.restapi.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/admin/categories")
	public ResponseEntity<CategoryResponse> getCategorys(
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		CategoryResponse response = categoryService.getCategorys(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@PostMapping("/admin/categories")
	public ResponseEntity<CategoryDTO> postCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategory = categoryService.createCategory(CategoryDTO);
		return new ResponseEntity<> (savedCategory, HttpStatus.CREATED);
	}
	
	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			@PathVariable Long categoryId) {
		CategoryDTO updatedProdDTO = categoryService.updateCategory(CategoryDTO, categoryId);
		return new ResponseEntity<> (updatedProdDTO, HttpStatus.OK);
	}
	
	@PutMapping("/admin/categories/delete/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		CategoryDTO CategoryDTO = categoryService.deleteCategory(categoryId);
		// return new ResponseEntity<>(status, HttpStatus.OK);
		// return ResponseEntity.ok(status);
	    return new ResponseEntity<> (CategoryDTO, HttpStatus.OK);
	}
}

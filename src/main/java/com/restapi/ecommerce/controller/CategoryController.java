package com.restapi.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	/**
	 * カテゴリーをすべて取得
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getCategories(
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = "categoryId",
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		CategoryResponse response = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	/**
	 * カテゴリーを追加
	 *
	 * @param categoryDTO
	 * @return
	 */
	@PostMapping("/admin/categories")
	public ResponseEntity<CategoryDTO> postCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<> (savedCategory, HttpStatus.CREATED);
	}

	/**
	 * カテゴリーを更新
	 *
	 * @param categoryDTO
	 * @param categoryId
	 * @return
	 */
	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			@PathVariable Long categoryId) {
		CategoryDTO updatedProdDTO = categoryService.updateCategory(categoryDTO, categoryId);
		return new ResponseEntity<> (updatedProdDTO, HttpStatus.OK);
	}

	/**
	 * カテゴリーを削除
	 *
	 * @param categoryId
	 * @return
	 */
	@DeleteMapping("/admin/categories/delete/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
	    return new ResponseEntity<> ("category deleted", HttpStatus.OK);
	}
}
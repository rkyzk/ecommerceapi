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
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;

	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getProducts(
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ProductResponse response = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = "productId",
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ProductResponse response = productService.getProductsByCategory(categoryId, pageNumber,
				pageSize, sortBy, sortOrder);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	@PostMapping("/admin/products")
	public ResponseEntity<ProductDTO> postProduct(@Valid @RequestBody ProductDTO productDTO) {
		ProductDTO savedProduct = productService.addProduct(productDTO);
		return new ResponseEntity<> (savedProduct, HttpStatus.CREATED);
	}

	@PutMapping("/admin/products/{prodId}")
	public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
			@PathVariable Long prodId) {
		ProductDTO updatedProdDTO = productService.updateProduct(productDTO, prodId);
		return new ResponseEntity<> (updatedProdDTO, HttpStatus.OK);
	}

	@PutMapping("/admin/products/delete/{prodId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long prodId) {
		ProductDTO productDTO = productService.deleteProduct(prodId);
		// return new ResponseEntity<>(status, HttpStatus.OK);
		// return ResponseEntity.ok(status);
	    return new ResponseEntity<> (productDTO, HttpStatus.OK);
	}

	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> searchProductsByKeyword(@PathVariable String keyword) {
		ProductResponse response = productService.searchProductsByKeyword(keyword);
		return new ResponseEntity<> (response, HttpStatus.FOUND);
	}
}
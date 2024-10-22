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
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getProducts() {
		ProductResponse response = productService.getProducts();
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@PostMapping("/admin/products")
	public ResponseEntity<Product> postProduct(@Valid @RequestBody Product product) {
		Product savedProduct = productService.createProduct(product);
		return new ResponseEntity<> (savedProduct, HttpStatus.CREATED);
	}
	
	@PutMapping("/admin/products/{prodId}")
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product,
			@PathVariable Long prodId) {
		Product updatedProduct = productService.updateProduct(product, prodId);
		return new ResponseEntity<> (updatedProduct, HttpStatus.OK);
	}
	
	@PutMapping("/admin/products/delete/{prodId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long prodId) {
		String status = productService.deleteProduct(prodId);
		// return new ResponseEntity<>(status, HttpStatus.OK);
		// return ResponseEntity.ok(status);
	    return ResponseEntity.status(HttpStatus.OK).body(status);
	}
}

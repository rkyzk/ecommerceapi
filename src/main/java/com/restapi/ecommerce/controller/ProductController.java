package com.restapi.ecommerce.controller;

import java.util.List;

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
import com.restapi.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/public/products")
	public ResponseEntity<List<Product>> getProducts() {
		List<Product> prodList = productService.getProducts();
		return new ResponseEntity<> (prodList, HttpStatus.OK);
	}
	
	@PostMapping("/admin/products")
	public ResponseEntity<Product> postProduct(@RequestBody Product product) {
		Product savedProduct = productService.createProduct(product);
		return new ResponseEntity<> (savedProduct, HttpStatus.CREATED);
	}
	
	@PutMapping("/admin/products/{prodId}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product,
			@PathVariable Long prodId) {
		Product updatedProduct = productService.updateProduct(product, prodId);
		return new ResponseEntity<> (updatedProduct, HttpStatus.OK);
	}
}

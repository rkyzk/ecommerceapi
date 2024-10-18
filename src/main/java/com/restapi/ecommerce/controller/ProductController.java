package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.entity.Product;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@GetMapping("/public/products")
	public ResponseEntity<List<Product>> getProducts() {
		List<Product> prodList = productService.getProducts();
		return new ResponseEntity<> (prodList, HttpStatus.OK);
	}
}

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
			@RequestParam (name = "pageNumber") Integer pageNumber,
			@RequestParam (name = "pageSize") Integer pageSize) {
		ProductResponse response = productService.getProducts(pageNumber, pageSize);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@PostMapping("/admin/products")
	public ResponseEntity<ProductDTO> postProduct(@Valid @RequestBody ProductDTO productDTO) {
		ProductDTO savedProduct = productService.createProduct(productDTO);
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
}

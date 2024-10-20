package com.restapi.ecommerce.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<Product> getProducts() {
		return productRepository.findAll();
	};
	
	@Override
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}
	
	@Override
	public Product updateProduct(Product product, Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);
		Product productToUpdate = storedProduct
				.orElseThrow(()
						-> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));
	    productToUpdate.setName(product.getName());
		productRepository.save(productToUpdate);
		return productToUpdate;
	}
	
	@Override
	public String deleteProduct(Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);	
		Product productToDelete = storedProduct
				.orElseThrow(()
						-> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));
	    productToDelete.setDeletedAt(Instant.now());
		productRepository.save(productToDelete);
		return "product deleted";
	}
}

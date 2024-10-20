package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.entity.Product;

public interface ProductService {
	public List<Product> getProducts();
	public Product createProduct(Product product);
	public Product updateProduct(Product product, Long prodId);
	public String deleteProduct(Long prodId);
}

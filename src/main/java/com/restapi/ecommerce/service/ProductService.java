package com.restapi.ecommerce.service;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.payload.ProductResponse;

public interface ProductService {
	ProductResponse getProducts();
	public Product createProduct(Product product);
	public Product updateProduct(Product product, Long prodId);
	public String deleteProduct(Long prodId);
}

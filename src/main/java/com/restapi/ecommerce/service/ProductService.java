package com.restapi.ecommerce.service;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;

public interface ProductService {
	ProductResponse getProducts();
	public ProductDTO createProduct(ProductDTO productDTO);
	public Product updateProduct(Product product, Long prodId);
	public String deleteProduct(Long prodId);
}

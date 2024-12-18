package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;

public interface ProductService {
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public ProductDTO addProduct(ProductDTO productDTO);
	public ProductDTO updateProduct(ProductDTO productDTO, Long prodId);
	public ProductDTO deleteProduct(Long prodId);
	// public ProductResponse searchByCategory(Long categoryId);
	public ProductResponse searchProductsByKeyword(String keyword);
}

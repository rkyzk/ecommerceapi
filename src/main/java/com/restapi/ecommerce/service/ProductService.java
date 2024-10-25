package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;

public interface ProductService {
	ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public ProductDTO createProduct(ProductDTO productDTO);
	public ProductDTO updateProduct(ProductDTO productDTO, Long prodId);
	public ProductDTO deleteProduct(Long prodId);
}

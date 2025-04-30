package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;

public interface ProductService {
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder, String keywords, String categoryId);
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
	public ProductDTO updateProduct(ProductDTO productDTO, Long prodId);
	public ProductDTO deleteProduct(Long prodId);
	public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortOrder);
	public ProductResponse searchProductsByKeywords(String keywords,
			Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
package com.restapi.ecommerce.repository;

import java.util.List;

import com.restapi.ecommerce.entity.Product;

public interface ProductRepositoryCustom {
	List<Product> getProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder, String categoryId, List<String> keywords, String colors);
        
	List<Product> getProductsSortBySalesCount(Integer pageNumber, Integer pageSize,
    		String categoryId, List<String> keywords, String colors);
	Long getTotalElements(String categoryId, List<String> keywords, String colors);
}

package com.restapi.ecommerce.repository;

import java.util.List;

import com.restapi.ecommerce.entity.Product;

public interface ProductRepositoryCustom {
	/**
	 * Get products filtered and sorted by given parameters.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return product list
	 */
	List<Product> getProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder, String categoryId, List<String> keywords, String colors);
  
	/**
	 * Get products filtered by given parameters
	 * sorted by sales count in the past 30 days.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return product list
	 */
	List<Product> getProductsSortBySalesCount(Integer pageNumber, Integer pageSize,
    		String categoryId, List<String> keywords, String colors);

	/**
	 * Get the number of products in the result set.
	 * 
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return number of results
	 */
	Long getTotalElements(String categoryId, List<String> keywords, String colors);
}

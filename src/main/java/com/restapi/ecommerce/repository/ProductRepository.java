package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	/** Get all products */
	Page<Product> findByDeletedAtIsNull(Pageable pageDetails);

	/**
	 * Get products filtered and sorted by given parameters.
	 */
	List<Product> getProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder, String categoryId, List<String> keywordList, String colorStr);

	/**
	 * Get products filtered by given parameters
	 * sorted by sales count in the past 30 days.
	 */
	List<Product> getProductsSortBySalesCount(Integer pageNumber, Integer pageSize,
    		String categoryId, List<String> keywords, String colors);

	/**
	 * Get the number of products in the result set.
	 */
	Long getTotalElements(String categoryId, List<String> keywordList, String colorStr);

	/**
	 * Get products filtered by category only.
	 */
	Page<Product> findByCategoryCategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageDetails);

	/**
	 * Get product by product name
	 *
	 * @param productName
	 * @return
	 */
	Product findByProductName(String productName);

	/** get fetured products (not in use) */
	List<Product> findByFeaturedIsTrue();

	/**
	 * update quantity(stock) of products
	 *
	 * @param id
	 * @param quantity
	 */
	@Modifying
	@Query(value="UPDATE products p SET p.quantity = ?2 "
			+ "where p.id = ?1", nativeQuery=true)
	void updateProductQuantity(Long id, Integer quantity);
}
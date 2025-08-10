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
public interface ProductRepository extends JpaRepository<Product, Long>{
	Page<Product> findByDeletedAtIsNull(Pageable pageDetails);
	Product findByProductName(String prodName);
	Page<Product> findByCategoryCategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageDetails);

	@Query(value = "SELECT DISTINCT p.* FROM products p INNER JOIN product_detail pd ON "
			+ "p.id = pd.product_id WHERE p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%'))))",
		   countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p INNER JOIN product_detail pd ON "
		    + "p.id = pd.product_id WHERE p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%'))))",
		   nativeQuery=true)
	Page<Product> findProductsByKeywords(
			String keyword, String keyword2, String keyword3, Pageable pageDetails);

	@Query(value = "SELECT DISTINCT p.* FROM products p INNER JOIN product_detail pd ON "
			+ "p.id = pd.product_id WHERE p.category_id = :categoryId AND p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%'))))",
		   countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p INNER JOIN product_detail pd ON "
			+ "p.id = pd.product_id WHERE p.category_id = :categoryId AND p.deleted_at is null AND "
	   		+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
	   		+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
	   		+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
	   		+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
	   		+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
	   		+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%'))))",
		   nativeQuery=true)
	Page<Product> findProductsByKeywordsAndCategory(
			String keyword, String keyword2, String keyword3, Long categoryId, Pageable pageDetails);

	List<Product> findByFeaturedIsTrue();

	@Modifying
	@Query(value="UPDATE products p SET p.quantity = ?2 "
			+ "where p.id = ?1", nativeQuery=true)
	void updateProductQuantity(Long id, Integer quantity);
}
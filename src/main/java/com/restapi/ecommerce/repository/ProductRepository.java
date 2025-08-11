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

	@Query(value = "SELECT p.* FROM products p WHERE "
			+ "p.deleted_at is null AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword,'%') OR p.description LIKE CONCAT('%',:keyword,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword2,'%') OR p.description LIKE CONCAT('%',:keyword2,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword3,'%') OR p.description LIKE CONCAT('%',:keyword3,'%'))",
		   countQuery = "SELECT COUNT(*) FROM products p WHERE "
			+ "p.deleted_at is null AND "
		    + "(p.product_name LIKE CONCAT('%',:keyword,'%') OR p.description LIKE CONCAT('%',:keyword,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword2,'%') OR p.description LIKE CONCAT('%',:keyword2,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword3,'%') OR p.description LIKE CONCAT('%',:keyword3,'%'))",
		   nativeQuery=true)
	Page<Product> findProductsByKeywords(
			String keyword, String keyword2, String keyword3, Pageable pageDetails);

	@Query(value = "SELECT p.* FROM products p WHERE "
			+ "p.category_id = :categoryId AND p.deleted_at is null AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword,'%') OR p.description LIKE CONCAT('%',:keyword,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword2,'%') OR p.description LIKE CONCAT('%',:keyword2,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword3,'%') OR p.description LIKE CONCAT('%',:keyword3,'%'))",
		   countQuery = "SELECT COUNT(*) FROM products p WHERE "
            + "p.category_id = :categoryId AND p.deleted_at is null AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword,'%') OR p.description LIKE CONCAT('%',:keyword,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword2,'%') OR p.description LIKE CONCAT('%',:keyword2,'%')) AND "
			+ "(p.product_name LIKE CONCAT('%',:keyword3,'%') OR p.description LIKE CONCAT('%',:keyword3,'%'))",
		   nativeQuery=true)
	Page<Product> findProductsByKeywordsAndCategory(
			String keyword, String keyword2, String keyword3, Long categoryId, Pageable pageDetails);

	List<Product> findByFeaturedIsTrue();

	@Modifying
	@Query(value="UPDATE products p SET p.quantity = ?2 "
			+ "where p.id = ?1", nativeQuery=true)
	void updateProductQuantity(Long id, Integer quantity);
}
package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	Product findByProductName(String prodName);
	@Query(value = "SELECT p.* FROM product p, product_category c "
	    + "WHERE p.product_id = c.product_id AND c.category_id =?1 ",
	    nativeQuery = true)
	List<Product> findByCategory(Long categoryId);
	List<Product> findByProductNameContainingIgnoreCase(String keyword);
}

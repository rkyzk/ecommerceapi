package com.restapi.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	Page<Product> findByDeletedAtIsNull(Pageable pageDetails);
	Product findByProductName(String prodName);
	Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageDetails);
	Page<Product> findByProductNameContainingIgnoreCase(String keyword,
			Pageable pageDetails);
}
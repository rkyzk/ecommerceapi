package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	// List<Product> findByCategory(Category category);
	List<Product> findByProductNameContainingIgnoreCase(String keyword);
}

package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findCartByUserUserIdAndOrderedAtIsNull(Long id);
	List<Cart> findAllByOrderedAtIsNull();
}

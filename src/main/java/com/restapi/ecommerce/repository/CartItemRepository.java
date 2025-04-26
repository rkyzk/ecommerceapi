package com.restapi.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	CartItem findByCartIdAndProductId(Long cartId, Long productId);

	// may not need this
	@Modifying
	@Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
	void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);
}

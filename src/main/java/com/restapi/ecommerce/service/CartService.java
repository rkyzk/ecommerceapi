package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.payload.CartDTO;

import jakarta.transaction.Transactional;

/** cart service interface */
public interface CartService {

	CartDTO addProductToCart(Long productId, Integer quantity);

	List<CartDTO> getAllCarts();

	CartDTO getCartByUser();

	@Transactional
	CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

	Cart updateTotalPrice(Cart cart);

	@Transactional
	String deleteProductFromCart(Long cartId, Long productId);
}

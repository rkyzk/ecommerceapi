package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.payload.CartDTO;

public interface CartService {
	CartDTO addProductToCart(Long productId, Integer quantity);
	List<CartDTO> getAllCarts();
}

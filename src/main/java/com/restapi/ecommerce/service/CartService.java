package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.CartDTO;

public interface CartService {
	CartDTO addProductToCart(Long productId, Integer quantity);

}

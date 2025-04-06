package com.restapi.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.payload.CartDTO;
import com.restapi.ecommerce.service.CartService;

@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartService cartService;

	@PostMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.addProductToCart(productId, quantity);		
		return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
	}
}

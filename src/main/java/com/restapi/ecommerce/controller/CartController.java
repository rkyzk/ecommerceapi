package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	/**
	 * add a product to cart
	 * and return the cart
	 *
	 * @param productId
	 * @param quantity
	 * @return
	 */
	@PostMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.addProductToCart(productId, quantity);		
		return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
	}

	@GetMapping("/carts")
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		List<CartDTO> carts = cartService.getAllCarts();
		return new ResponseEntity<>(carts, HttpStatus.FOUND);
	}

	@GetMapping("/carts/user/cart")
	public ResponseEntity<CartDTO> getCartByUser() {
		CartDTO cartDTO = cartService.getCartByUser();
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
		
	}
}

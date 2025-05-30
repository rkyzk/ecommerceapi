package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	 * @return cart
	 */
	@PostMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.addProductToCart(productId, quantity);		
		return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
	}

	/**
	 * update quantity of product in cart
	 * 
	 * @param productId
	 * @param operation
	 * @return cartDTO
	 */
	@PutMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> updateProductQuantityInCart(@PathVariable Long productId,
			@PathVariable int quantity) {
		CartDTO cartDTO = cartService.updateProductQuantityInCart(
				productId, quantity);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
	}

	/**
	 * delete a product from a given cart
	 * 
	 * @param cartId
	 * @param productId
	 * @return
	 */
	@DeleteMapping("/carts/{cartId}/products/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
			@PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

	/**
	 * get all carts that haven't been checked out
	 *
	 * @return list of carts
	 */
	@GetMapping("/carts")
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		List<CartDTO> carts = cartService.getAllCarts();
		return new ResponseEntity<>(carts, HttpStatus.FOUND);
	}

	/**
	 * get cart of logged in user
	 * @return cartDTO
	 */
	@GetMapping("/carts/user/cart")
	public ResponseEntity<CartDTO> getCartByUser() {
		CartDTO cartDTO = cartService.getCartByUser();
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
		
	}
}

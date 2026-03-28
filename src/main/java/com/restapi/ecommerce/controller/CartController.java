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

/**
 * Controller that handles getting/updating cart data
 * 
 * @author reikoyazaki
 *
 */
@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartService cartService;

	/**
	 * Add products to cart and return cart data
	 *
	 * @param productId
	 * @param quantity
	 *
	 * @return cart
	 */
	@PostMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.addProductToCart(productId, quantity);		
		return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
	}

	/**
	 * Update product quntity in the cart
	 * 
	 * @param productId
	 * @param quantity
	 *
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
	 * Remove a specific product from cart
	 * 
	 * @param cartId
	 * @param productId
	 *
	 * @return
	 */
	@DeleteMapping("/carts/{cartId}/products/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
			@PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

	/**
	 * get a list of carts
	 *
	 * @return list of carts
	 */
	@GetMapping("/carts")
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		List<CartDTO> carts = cartService.getAllCarts();
		return new ResponseEntity<>(carts, HttpStatus.FOUND);
	}

	/**
	 * get cart data of the logged in user.
	 * 
	 * @return cartDTO
	 */
	@GetMapping("/carts/user/cart")
	public ResponseEntity<CartDTO> getCartByUser() {
		CartDTO cartDTO = cartService.getCartByUser();
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
		
	}
}

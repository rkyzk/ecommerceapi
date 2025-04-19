package com.restapi.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.CartDTO;
import com.restapi.ecommerce.repository.CartItemRepository;
import com.restapi.ecommerce.repository.CartRepository;
import com.restapi.ecommerce.repository.ProductRepository;
import com.restapi.ecommerce.utils.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuthUtil authUtil;

	public CartDTO addProductToCart(Long productId, Integer quantity) {
		// Get the user's cart.  If there isn't one, make a new one.
		Cart cart = getCart();

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		Integer stock = product.getQuantity();

		if (stock == 0) {
			throw new APIException("We're sorry.  The Product is out of stock");
		}
		if (quantity > stock) {
			// TO BE CORRECTED!  Return message, not excpetion
			throw new APIException("Only " + stock +
					(stock > 1 ? " are" : " is") + " available. " +
					"Would you like to order " + stock + "?");
		}

		CartItem item = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getId());
		if (item != null) {
			item.setQuantity(item.getQuantity() + quantity);
		} else {
			item = new CartItem(product, quantity, cart);
		}
		cartItemRepository.save(item);
		// update product quantity(stock)
		product.setQuantity(stock - quantity);
		// update the total price in Cart entity
		cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantity);
		
		cartRepository.save(cart);
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		return cartDTO;
	}


	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepository.findAll();
		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			return cartDTO;
		}).collect(Collectors.toList());
		return cartDTOs;
	}

	/**
	 * update quantity of product in cart
	 * 
	 * @param productId
	 * @param operation
	 * @return cartDTO
	 */
	@Transactional
	@Override
	public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
		// if product is not found, return error message
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
		CartDTO cartDTO = getCartByUser();
		Long cartId = cartDTO.getCartId();
		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(
				cartId, productId);
		if (cartItem == null) {
			throw new ResourceNotFoundException("CartItem", "productId", productId);
		}
		if (quantity == 0) cartItemRepository.delete(cartItem);
		int origQty = cartItem.getQuantity();
		int stock = product.getQuantity();
		if (stock == 0) {
			new APIException("We're sorry.  The product just got sold out.");
		}
		if (origQty + stock < quantity) {
			throw new APIException("Only " + stock +
					(stock > 1 ? " are" : " is") + " available. " +
					"Would you like to order " + stock + "?");
		}
		// update product quantity(stock)
		product.setQuantity(stock - (quantity - origQty));
		productRepository.save(product);
		// update cart item quantity
		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
		Cart cart = updateTotalPrice(cartRepository.getReferenceById(cartId));
		CartDTO updatedDTO = modelMapper.map(cart, CartDTO.class);
		return updatedDTO;
	}

	/**
	 * Return cart of logged-in user
	 * 
	 * @return cart data
	 */
	@Override
	public CartDTO getCartByUser() {
		User user = authUtil.loggedinUser();
		Cart cart = cartRepository.findCartByUserUserId(user.getUserId());
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "user", user.getUsername());
		}
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		return cartDTO;
	}

	/**
	 * If the user already has a cart, return it.
	 * Otherwise create and return a new cart.
	 * 
	 * @return Cart object
	 */
	public Cart getCart() {
		Cart cart = cartRepository.findCartByUserUserId(authUtil.loggedinUser().getUserId());
		if (cart == null) {
			cart = new Cart(authUtil.loggedinUser());
			cartRepository.save(cart);
		}
		return cart;
	}

	/**
	 * update the total price of the cart
	 */
	@Override
	public Cart updateTotalPrice(Cart cart) {
		double price = 0.0;
		for (CartItem item: cart.getCartItems()) {
			price += item.getProduct().getPrice() * item.getQuantity();
		}
		cart.setTotalPrice(price);
		cartRepository.save(cart);
		return cartRepository.getReferenceById(cart.getId());
	}
}

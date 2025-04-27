package com.restapi.ecommerce.service;

import java.util.List;
import java.util.Set;
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

	@Override
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
			// TO BE CORRECTED
			throw new APIException("Only " + stock +
					(stock > 1 ? " are" : " is") + " available. " +
					"Would you like to order " + stock + "?");
		}

		CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
		if (item != null) {
			item.setQuantity(item.getQuantity() + quantity);
		} else {
			item = new CartItem(product, quantity, cart);
		}
		cartItemRepository.save(item);
		// update the total price in Cart entity
		cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantity);
		cartRepository.save(cart);
		Set<CartItem> set = cart.getCartItems();
		set.add(item);
		cart.setCartItems(set);
		return modelMapper.map(cart, CartDTO.class);
	}

	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepository.findAllByOrderedIsFalse();
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
		CartDTO cartDTO = getCartByUser();
		Long cartId = cartDTO.getCartId();
		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(
				cartId, productId);
		if (cartItem == null) {
			throw new ResourceNotFoundException("CartItem", "productId", productId);
		}
		if (quantity == 0) cartItemRepository.delete(cartItem);
		int origQty = cartItem.getQuantity();
		int stock = cartItem.getProduct().getQuantity();
		if (stock == 0) {
			new APIException("We're sorry.  The product is out of stock.");
		}
		if (origQty + stock < quantity) {
			throw new APIException("Only " + stock +
					(stock > 1 ? " are" : " is") + " available. " +
					"Would you like to order " + stock + "?");
		}
		// update cart item quantity
		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() ->
				new ResourceNotFoundException("Cart", "id", cartId));
		Cart updatedCart = updateTotalPrice(cart);
		return modelMapper.map(updatedCart, CartDTO.class);
	}

	@Transactional
	@Override
	public String deleteProductFromCart(Long cartId, Long productId) {
		CartItem item = cartItemRepository.findByCartIdAndProductId(cartId, productId);
		if (item == null) throw new APIException(
				"Cart item with the given product ID and cart ID was not found");
		// delete the item
		cartItemRepository.delete(item);
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
		Set<CartItem> set = cart.getCartItems();
		set.remove(item);
		if (set.size() == 0) {
			cartRepository.delete(cart);
			return "The cart is empty.";
		}
		cart.setCartItems(set);
		updateTotalPrice(cart);
		return "Product " + item.getProduct().getProductName() + " was removed from the cart.";
	}

	/**
	 * Return cart of logged-in user
	 * 
	 * @return cart data
	 */
	@Override
	public CartDTO getCartByUser() {
		User user = authUtil.loggedinUser();
		Cart cart = cartRepository.findCartByUserUserIdAndOrderedIsFalse(user.getUserId());
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
		Cart cart = cartRepository.findCartByUserUserIdAndOrderedIsFalse(
				authUtil.loggedinUser().getUserId());
		if (cart == null) {
			cart = new Cart(authUtil.loggedinUser());
			cartRepository.save(cart);
		}
		return cart;
	}

	/**
	 * update the total price of a given cart
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

package com.restapi.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.CartDTO;
import com.restapi.ecommerce.repository.CartItemRepository;
import com.restapi.ecommerce.repository.CartRepository;
import com.restapi.ecommerce.repository.ProductRepository;
import com.restapi.ecommerce.utils.AuthUtil;

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
		Integer productStock = product.getQuantity();

		if (productStock == 0) {
			new APIException("We're sorry.  The Product is out of stock");
		}
		if (quantity > productStock) {
			// TO BE CORRECTED!  Return message, not excpetion
			new APIException("There are only " + productStock + " in the stock."
					+ "Would you like to order " + productStock + " of this product?");
		}

		CartItem item = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getId());
		if (item != null) {
			item.setQuantity(item.getQuantity() + quantity);
		} else {
			item = new CartItem(product, quantity, cart);
		}
		cartItemRepository.save(item);
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
	 * If the user already has a cart, return it.
	 * Otherwise create and return a new cart.
	 * 
	 * @return Cart object
	 */
	public Cart getCart() {
		Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedinUser().getEmail());
		if (cart == null) {
			cart = new Cart(authUtil.loggedinUser());
			cartRepository.save(cart);
		}
		return cart;
	}
}

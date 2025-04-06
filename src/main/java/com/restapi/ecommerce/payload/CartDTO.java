package com.restapi.ecommerce.payload;

import java.util.ArrayList;
import java.util.List;

import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.User;

public class CartDTO {
	private long cartId;

	private User user;

	private List<CartItem> cartItems = new ArrayList<>();

	private double totalPrice = 0.0;
}

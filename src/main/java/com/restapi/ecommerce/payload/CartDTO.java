package com.restapi.ecommerce.payload;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
	private Long cartId;

	private User user;

	private Set<CartItem> cartItems = new HashSet<>();

	private Double totalPrice = 0.0;

	private Instant ordered_at;
}

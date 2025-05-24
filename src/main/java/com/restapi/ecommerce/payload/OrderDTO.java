package com.restapi.ecommerce.payload;

import java.time.Instant;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.User;

import lombok.Data;

@Data
public class OrderDTO {
	private Long orderId;
	private Instant orderDate;
	private String orderStatus;
	private Cart cart;
	private User user;
	private PaymentDTO paymentDTO;
	private Address shippingAddress;
	private Address billingAddress;
}

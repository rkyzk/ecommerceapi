package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestByGuestDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;

import jakarta.transaction.Transactional;

public interface OrderService {
	@Transactional
	OrderDTO placeOrder(Long cartId, OrderRequestDTO orderRequestDTO);

	@Transactional
	OrderDTO placeOrderAsGuest(OrderRequestByGuestDTO orderRequestByGuestDTO);
}

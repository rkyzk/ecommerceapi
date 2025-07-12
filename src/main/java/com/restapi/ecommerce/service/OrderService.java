package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithSavedAddressDTO;

import jakarta.transaction.Transactional;

/** order service interface */
public interface OrderService {
	@Transactional
	OrderDTO placeOrder(Long cartId, OrderRequestWithSavedAddressDTO orderRequestDTO);

	@Transactional
	OrderDTO placeOrderAsGuest(OrderRequestDTO orderRequestByGuestDTO);

	@Transactional
	OrderDTO placeOrderAsUser(OrderRequestDTO orderRequestDTO);

	@Transactional
	OrderDTO placeOrderWithNewAddresses(OrderRequestDTO orderRequestDTO);

}

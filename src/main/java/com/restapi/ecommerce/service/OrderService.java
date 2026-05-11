package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithAddressesDTO;
import com.restapi.ecommerce.payload.OrderResponse;

import jakarta.transaction.Transactional;

/** order service interface */
public interface OrderService {
	@Transactional
	OrderDTO placeOrder(OrderRequestDTO orderRequestDTO);

	@Transactional
	OrderDTO placeOrderWithNewAddresses(OrderRequestWithAddressesDTO orderRequestDTO);

	OrderResponse getUserOrderList(Integer pageNumber, Integer pageSize, String sortOrder);
}

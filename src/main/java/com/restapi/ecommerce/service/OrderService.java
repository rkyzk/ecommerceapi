package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithAddressesDTO;

import jakarta.transaction.Transactional;

/** order service interface */
public interface OrderService {
	@Transactional
	OrderDTO placeOrder(OrderRequestDTO orderRequestDTO);

	@Transactional
	OrderDTO placeOrderWithNewAddresses(OrderRequestWithAddressesDTO orderRequestDTO);
	List<OrderDTO> getUserOrderList();
}

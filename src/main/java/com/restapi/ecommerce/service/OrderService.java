package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;

public interface OrderService {
	OrderDTO placeOrder(Long cartId, OrderRequestDTO orderRequestDTO);

}

package com.restapi.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	OrderService orderService;

	@PostMapping("/order/cart/{cartId}")
	public ResponseEntity<OrderDTO> createOrder(@PathVariable Long cartId,
			@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrder(cartId, orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);		
	}
}

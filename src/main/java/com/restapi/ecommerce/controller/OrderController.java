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
import com.restapi.ecommerce.payload.OrderRequestWithSavedAddressDTO;
import com.restapi.ecommerce.payload.StripePaymentDTO;
import com.restapi.ecommerce.service.OrderService;
import com.restapi.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private StripeService stripeService;

	/**
	 * 注文データを追加
	 *
	 * @param cartId
	 * @param orderRequestDTO
	 * @return OrderDTO
	 */
	@PostMapping("/order/cart/{cartId}")
	public ResponseEntity<OrderDTO> createOrder(@PathVariable Long cartId,
			@Valid @RequestBody OrderRequestWithSavedAddressDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrder(cartId, orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * 注文データを追加（ゲスト）
	 *
	 * @param orderRequestByGuestDTO
	 * @return
	 */
	@PostMapping("/order/guest")
	public ResponseEntity<OrderDTO> placeOrderAsGuest(@RequestBody
			OrderRequestDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrderAsGuest(orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * 注文データを追加（ログインユーザ・既存住所更新なし）
	 *
	 * @param orderRequestDTO
	 * @return
	 */
	@PostMapping("/order")
	public ResponseEntity<OrderDTO> placeOrderAsUser(@RequestBody
			OrderRequestDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrderAsUser(orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * 注文データを追加（ログインユーザ・既存住所更新あり）
	 *
	 * @param orderRequestDTO
	 * @return
	 */
	@PostMapping("/order/address/add")
	public ResponseEntity<OrderDTO> placeOrderAsUserAddAddress(@RequestBody
			OrderRequestDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrderAsUserAddAddress(orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * ペイメントインテント作成、ClientSecretを返却
	 *
	 * @param stripePaymentDto
	 * @return
	 * @throws StripeException
	 */
    @PostMapping("/order/stripe-client-secret")
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentDTO stripePaymentDto)
    		throws StripeException {
            PaymentIntent paymentIntent = stripeService.paymentIntent(stripePaymentDto);
            return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }
}

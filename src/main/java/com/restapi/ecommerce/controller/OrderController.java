package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.payload.APIResponse;
import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithAddressesDTO;
import com.restapi.ecommerce.payload.StripePaymentDTO;
import com.restapi.ecommerce.service.OrderService;
import com.restapi.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

/**
 * Controller that handles
 * requests for placing orders and for creating client secrets
 * 
 * @author reikoyazaki
 *
 */
@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private StripeService stripeService;

	/**
	 * Place order (without registering new addresses)
	 *
	 * @param orderRequestDTO
	 * @return
	 */
	@PostMapping("/order")
	public ResponseEntity<OrderDTO> placeOrder(@RequestBody
			OrderRequestDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrder(orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * Place order (register new addresses)
	 * 
	 * @param orderRequestDTO
	 * @return
	 */
	@PostMapping("/order/newaddresses")
	public ResponseEntity<OrderDTO> placeOrderWithNewAddresses(@RequestBody
			OrderRequestWithAddressesDTO orderRequestDTO) {
		OrderDTO placedOrderDTO = orderService.placeOrderWithNewAddresses(orderRequestDTO);
		return new ResponseEntity<OrderDTO>(placedOrderDTO, HttpStatus.CREATED);
	}

	/**
	 * Get logged-in user's order history
	 * 
	 * @param orderRequestDTO
	 * @return
	 */
	@GetMapping("/order-history")
	public ResponseEntity<?> getUserOrderHistory() {
		List<OrderDTO> orderList = orderService.getUserOrderList();
		if (orderList == null) {
			APIResponse response = new APIResponse();
			response.setMessage("No order history available.");
			return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);
	}

	/**
	 * Create a payment intent and return clientSecret
	 * using Stripe API
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

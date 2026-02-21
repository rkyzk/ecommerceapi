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
 * 注文に関するリクエストを処理するコントローラー
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
	 * 注文データを受けDB登録処理を呼び出す
	 * 注文データを返却する
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
	 * 注文を受けDB登録処理を呼び出す(新規住所登録を含む)
	 * 注文データを返却する
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
	 * ログイン中ユーザの全注文データを取得し返却する
	 * 
	 * @param orderRequestDTO
	 * @return
	 */
	@GetMapping("/order-history")
	public ResponseEntity<?> getUserOrderHistory() {
		List<OrderDTO> orderList = orderService.getUserOrderList();
		if (orderList == null) {
			APIResponse response = new APIResponse();
			response.setMessage("購入履歴はありません。");
			return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);
	}

	/**
	 * Stripe APIによりPayment intent作成し clientSecretを返却する
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

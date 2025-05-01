package com.restapi.ecommerce.service;

import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.Payment;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.PaymentDTO;
import com.restapi.ecommerce.repository.AddressRepository;
import com.restapi.ecommerce.repository.CartRepository;
import com.restapi.ecommerce.repository.OrderRepository;
import com.restapi.ecommerce.repository.PaymentRepository;
import com.restapi.ecommerce.repository.ProductRepository;
import com.restapi.ecommerce.utils.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	AuthUtil authUtil;

	@Autowired
	ModelMapper modelMapper;

	@Override
	@Transactional
	public OrderDTO placeOrder(Long cartId, OrderRequestDTO orderRequestDTO) {
		User user = authUtil.loggedinUser();
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setUser(user);
		orderDTO.setCart(cart);
		orderDTO.setOrderDate(Instant.now());
		orderDTO.setOrderStatus("placed");
		Address address = addressRepository.findById(orderRequestDTO.getAddressId())
				.orElseThrow(() ->
					new ResourceNotFoundException("Address", "id", orderRequestDTO.getAddressId()));
		orderDTO.setAddress(address);
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setPgName(orderRequestDTO.getPgName());
		Payment payment = modelMapper.map(paymentDTO, Payment.class);
		Payment savedPayment = paymentRepository.save(payment);
		Order order = modelMapper.map(orderDTO, Order.class);
		order.setPayment(savedPayment);
		Order savedOrder = orderRepository.save(order);
		cart.setOrdered(true);
		cartRepository.save(cart);
		// reduce the product stock
		cart.getCartItems().forEach(item -> {
			Product product = item.getProduct();
			product.setQuantity(item.getProduct().getQuantity() - item.getQuantity());
			productRepository.save(product);
		});
		OrderDTO savedOrderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		savedOrderDTO.setPaymentDTO(paymentDTO);
		return savedOrderDTO;
	}
}

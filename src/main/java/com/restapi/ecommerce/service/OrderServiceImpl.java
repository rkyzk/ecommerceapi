package com.restapi.ecommerce.service;



import java.time.Instant;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.Payment;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithSavedAddressDTO;
import com.restapi.ecommerce.repository.AddressRepository;
import com.restapi.ecommerce.repository.CartItemRepository;
import com.restapi.ecommerce.repository.CartRepository;
import com.restapi.ecommerce.repository.OrderRepository;
import com.restapi.ecommerce.repository.PaymentRepository;
import com.restapi.ecommerce.repository.ProductRepository;
import com.restapi.ecommerce.utils.AuthUtil;

import jakarta.transaction.Transactional;

/** order service implementation */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartRepository cartRepository;

    @Autowired
	CartItemRepository cartItemRepository;

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

	@Autowired
	AddressService addressService;

	@Override
	@Transactional
	public OrderDTO placeOrder(Long cartId, OrderRequestWithSavedAddressDTO orderRequestDTO) {
//		User user = authUtil.loggedinUser();
//		Cart cart = cartRepository.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setUser(user);
//		orderDTO.setCart(cart);
//		orderDTO.setOrderDate(Instant.now());
//		orderDTO.setOrderStatus("placed");
//		Address address = addressRepository.findById(orderRequestDTO.getAddressId())
//				.orElseThrow(() ->
//					new ResourceNotFoundException("Address", "id", orderRequestDTO.getAddressId()));
//		orderDTO.setAddress(address);
//		PaymentDTO paymentDTO = new PaymentDTO(
//				orderRequestDTO.getPgPaymentId(),
//				orderRequestDTO.getPgStatus(),
//				orderRequestDTO.getPgResponseMessage(),
//				orderRequestDTO.getPgName());
//		Payment payment = modelMapper.map(paymentDTO, Payment.class);
//		Payment savedPayment = paymentRepository.save(payment);
//		Order order = modelMapper.map(orderDTO, Order.class);
//		order.setPayment(savedPayment);
//		Instant currTime = Instant.now();
//		order.setOrderDate(currTime);
//		Order savedOrder = orderRepository.save(order);
//		cart.setOrderedAt(currTime);
//		cartRepository.save(cart);
//		// reduce the product stock
//		cart.getCartItems().forEach(item -> {
//			Product product = item.getProduct();
//			product.setQuantity(item.getProduct().getQuantity() - item.getQuantity());
//			productRepository.save(product);
//		});
//		OrderDTO savedOrderDTO = modelMapper.map(savedOrder, OrderDTO.class);
//		savedOrderDTO.setPaymentDTO(paymentDTO);
//		return savedOrderDTO;
		return null;
	}

//	@Transactional
//	@Override
//	public OrderDTO placeOrderAsGuest(OrderRequestDTO orderRequestByGuestDTO) {
//		List<AddressDTO> addressDTOList = orderRequestByGuestDTO.getAddressDTOList();
//		Address shippingAddress = modelMapper.map(addressDTOList.get(0),Address.class);
//		Address savedSAddress = addressRepository.save(shippingAddress);
//		Address billingAddress = null;
//		Address savedBAddress = null;
//		if (addressDTOList.size() > 1) {
//			billingAddress = modelMapper.map(addressDTOList.get(1),Address.class);
//		    savedBAddress = addressRepository.save(billingAddress);
//		}
//		Cart cart = modelMapper.map(orderRequestByGuestDTO.getCartDTO(), Cart.class);
//		Cart newCart = cartRepository.save(cart);
//		Set<CartItem> items = orderRequestByGuestDTO.getCartDTO().getCartItems();
//		for (CartItem item: items) {
//			item.setCart(newCart);
//			cartItemRepository.save(item);
//		}
//		Cart cartToUpdate = cartRepository.findById(newCart.getId())
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", newCart.getId()));
//		Instant currTime = Instant.now();
//		cartToUpdate.setOrderedAt(currTime);
//		Cart savedCart = cartRepository.save(cart);
//		Order newOrder = new Order(currTime, savedCart, null, null, savedSAddress, savedBAddress);
//		Order placedOrder = orderRepository.save(newOrder);
//		OrderDTO placedOrderDTO = modelMapper.map(placedOrder, OrderDTO.class);
//		return placedOrderDTO;
//	}

	@Transactional
	@Override
	public OrderDTO placeOrderAsUser(OrderRequestDTO orderRequestDTO) {
		User user = authUtil.loggedinUser();
		// get saved addresses
		Address shippingAddress = addressRepository.findByUserUserIdAndBillingAddressIsFalse(user.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Address", "userId", user.getUserId()));
		Address billingAddress = addressRepository.findByUserUserIdAndBillingAddressIsTrue(user.getUserId());

		Cart cart = modelMapper.map(orderRequestDTO.getCartDTO(), Cart.class);
		Cart newCart = cartRepository.save(cart);
		Set<CartItem> items = orderRequestDTO.getCartDTO().getCartItems();
		for (CartItem item: items) {
			item.setCart(newCart);
			cartItemRepository.save(item);
		}
		Cart cartToUpdate = cartRepository.findById(newCart.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", newCart.getId()));
		Instant currTime = Instant.now();
		cartToUpdate.setOrderedAt(currTime);
		cart.setUser(user);
		Cart savedCart = cartRepository.save(cart);

		Payment payment = new Payment(orderRequestDTO.getPgPaymentId(),
				orderRequestDTO.getPgStatus(), orderRequestDTO.getPgResponseMessage(),
				orderRequestDTO.getPgName());
		Payment savedPayment = paymentRepository.save(payment);

		Order newOrder = new Order(currTime, savedCart, user, savedPayment, shippingAddress, billingAddress);
		Order placedOrder = orderRepository.save(newOrder);
		OrderDTO placedOrderDTO = modelMapper.map(placedOrder, OrderDTO.class);
		return placedOrderDTO;
	}

	@Transactional
	@Override
	public OrderDTO placeOrderWithNewAddresses(OrderRequestDTO orderRequestDTO) {
		// get addresses
		Address shippingAddress = addressRepository.findById(orderRequestDTO.getShippingAddressId())
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", orderRequestDTO.getShippingAddressId()));
		Address billingAddress = null;
		if (orderRequestDTO.getBillingAddressId() != null) {
			billingAddress = addressRepository.findById(orderRequestDTO.getBillingAddressId())
					.orElseThrow(() -> new ResourceNotFoundException("Address", "id", orderRequestDTO.getBillingAddressId()));
		}
		// save cart
		User user = authUtil.loggedinUser();
		Cart cart = modelMapper.map(orderRequestDTO.getCartDTO(), Cart.class);
		Cart newCart = cartRepository.save(cart);
		Set<CartItem> items = orderRequestDTO.getCartDTO().getCartItems();
		for (CartItem item: items) {
			item.setCart(newCart);
			cartItemRepository.save(item);
		}
		Cart cartToUpdate = cartRepository.findById(newCart.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", newCart.getId()));
		Instant currTime = Instant.now();
		cartToUpdate.setOrderedAt(currTime);
		cart.setUser(user);
		Cart savedCart = cartRepository.save(cart);

		Payment payment = new Payment(orderRequestDTO.getPgPaymentId(),
				orderRequestDTO.getPgStatus(), orderRequestDTO.getPgResponseMessage(),
				orderRequestDTO.getPgName());
		Payment savedPayment = paymentRepository.save(payment);

		Order newOrder = new Order(currTime, savedCart, user, savedPayment, shippingAddress, billingAddress);
		Order placedOrder = orderRepository.save(newOrder);
		OrderDTO placedOrderDTO = modelMapper.map(placedOrder, OrderDTO.class);
		return placedOrderDTO;
	}

	@Transactional
	@Override
	public OrderDTO placeOrderAsGuest(OrderRequestDTO orderRequestDTO) {
//		List<AddressDTO> addressDTOList = orderRequestDTO.getAddressDTOList();
//		Address shippingAddress = modelMapper.map(addressDTOList.get(0), Address.class);
//		Address savedSAddress = addressRepository.save(shippingAddress);
//		Address billingAddress = null;
//		Address savedBAddress = null;
//		if (addressDTOList.size() > 1) {
//			billingAddress =  modelMapper.map(addressDTOList.get(1), Address.class);
//			savedBAddress = addressRepository.save(billingAddress);
//		}
//		Cart cart = modelMapper.map(orderRequestDTO.getCartDTO(), Cart.class);
//		Cart newCart = cartRepository.save(cart);
//		Set<CartItem> items = orderRequestDTO.getCartDTO().getCartItems();
//		for (CartItem item: items) {
//			item.setCart(newCart);
//			cartItemRepository.save(item);
//		}
//		Cart cartToUpdate = cartRepository.findById(newCart.getId())
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "id", newCart.getId()));
//		Instant currTime = Instant.now();
//		cartToUpdate.setOrderedAt(currTime);
//		Cart savedCart = cartRepository.save(cart);
//
//		Payment payment = new Payment(orderRequestDTO.getPgPaymentId(),
//				orderRequestDTO.getPgStatus(), orderRequestDTO.getPgResponseMessage(),
//				orderRequestDTO.getPgName());
//		Payment savedPayment = paymentRepository.save(payment);
//		Order newOrder = new Order(currTime, savedCart, null, savedPayment, savedSAddress, savedBAddress);
//		Order placedOrder = orderRepository.save(newOrder);
//		OrderDTO placedOrderDTO = modelMapper.map(placedOrder, OrderDTO.class);
		return new OrderDTO ();
	}
}

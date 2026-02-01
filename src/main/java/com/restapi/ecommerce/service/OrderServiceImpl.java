package com.restapi.ecommerce.service;



import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Cart;
import com.restapi.ecommerce.entity.CartItem;
import com.restapi.ecommerce.entity.CompositePrimaryKey;
import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.Payment;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.entity.SalesCount;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.payload.OrderDTO;
import com.restapi.ecommerce.payload.OrderRequestDTO;
import com.restapi.ecommerce.payload.OrderRequestWithAddressesDTO;
import com.restapi.ecommerce.repository.AddressRepository;
import com.restapi.ecommerce.repository.CartItemRepository;
import com.restapi.ecommerce.repository.CartRepository;
import com.restapi.ecommerce.repository.OrderRepository;
import com.restapi.ecommerce.repository.PaymentRepository;
import com.restapi.ecommerce.repository.ProductRepository;
import com.restapi.ecommerce.repository.SalesCountRepository;
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
	SalesCountRepository salesCountRepository;

	@Autowired
	AuthUtil authUtil;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AddressService addressService;

	@Transactional
	@Override
	public OrderDTO placeOrder(OrderRequestDTO orderRequestDTO) {
		User user = authUtil.loggedinUser();
		// get saved addresses
		Address shippingAddress = addressRepository.findById(orderRequestDTO.getShippingAddressId())
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", orderRequestDTO.getShippingAddressId()));
		Address billingAddress = null;
		if (orderRequestDTO.getBillingAddressId() != 0) {
			billingAddress = addressRepository.findById(orderRequestDTO.getBillingAddressId())
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", orderRequestDTO.getBillingAddressId()));
		}
		Cart cart = modelMapper.map(orderRequestDTO.getCartDTO(), Cart.class);
		Cart newCart = cartRepository.save(cart);
		Set<CartItem> items = orderRequestDTO.getCartDTO().getCartItems();
		for (CartItem item: items) {
			item.setCart(newCart);
			cartItemRepository.save(item);
			// 商品の在庫数を更新
			Product product = productRepository.findById(item.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.getProduct().getId()));
			product.setQuantity(product.getQuantity() - item.getQuantity());
			productRepository.save(product);
			// 商品の売上数を更新
			updateSalesQuantity(product, item.getQuantity());
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
	public OrderDTO placeOrderWithNewAddresses(OrderRequestWithAddressesDTO orderRequestDTO) {
		User user = authUtil.loggedinUser();
		// save addresses
		Long shippingAddressId = orderRequestDTO.getShippingAddressDTO().getAddressId();
		Address shippingAddress = null;
		if (shippingAddressId == 0) {
			AddressDTO savedSAddressDTO = addressService.addAddress(orderRequestDTO.getShippingAddressDTO(),
					orderRequestDTO.getShippingAddressDTO().getUser() != null ? user : null);
			shippingAddress = modelMapper.map(savedSAddressDTO, Address.class);
		} else {
			shippingAddress = addressRepository.findById(shippingAddressId)
					.orElseThrow(() -> new ResourceNotFoundException("Address", "id", shippingAddressId));
		}
		Address billingAddress = null;
		if (orderRequestDTO.getBillingAddressDTO() != null) {
			Long billingAddressId = orderRequestDTO.getBillingAddressDTO().getAddressId();
			if (billingAddressId == -1) {
				AddressDTO savedBAddressDTO = addressService.addAddress(orderRequestDTO.getBillingAddressDTO(),
						orderRequestDTO.getBillingAddressDTO().getUser() != null ? user : null);
				billingAddress = modelMapper.map(savedBAddressDTO, Address.class);
			} else if (billingAddressId > 0){
				billingAddress = addressRepository.findById(billingAddressId)
						.orElseThrow(() -> new ResourceNotFoundException("Address", "id", billingAddressId));
			}
		}
		// save cart
		Cart cart = modelMapper.map(orderRequestDTO.getCartDTO(), Cart.class);
		Cart newCart = cartRepository.save(cart);
		Set<CartItem> items = orderRequestDTO.getCartDTO().getCartItems();
		for (CartItem item: items) {
			item.setCart(newCart);
			cartItemRepository.save(item);
			// 商品の在庫数を更新
			Product product = productRepository.findById(item.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.getProduct().getId()));
			product.setQuantity(product.getQuantity() - item.getQuantity());
			productRepository.save(product);
			// 商品の売上数を更新
			updateSalesQuantity(product, item.getQuantity());
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
		Order newOrder = new Order(currTime, savedCart, user, savedPayment,
				shippingAddress, billingAddress);
		Order placedOrder = orderRepository.save(newOrder);
		OrderDTO placedOrderDTO = modelMapper.map(placedOrder, OrderDTO.class);
		return placedOrderDTO;
	}

	/**
	 * 指定するユーザIDの注文のリストを取得
	 * 
	 * @param userId: ユーザID
	 * @return 注文リスト
	 */
	@Override
	public List<OrderDTO> getUserOrderList() {
		User user = authUtil.loggedinUser();
		List<Order> orders = orderRepository.findByUserUserIdOrderByOrderDateDesc(user.getUserId());
		if (orders == null) return null;
		return orders.stream()
				.map(order -> modelMapper.map(order, OrderDTO.class))
								.toList();
	}

	/**
	 * 商品ごとの売上個数を保存
	 *
	 * @param product
	 * @param quantity
	 */
	private void updateSalesQuantity(Product product, int quantity) {
		LocalDate date = LocalDate.now();
		SalesCount salesCnt = salesCountRepository.findById(new CompositePrimaryKey(product, date))
				.orElse(salesCountRepository.save(new SalesCount(product, date, quantity)));
		if (salesCnt != null) {
			salesCnt.setQuantity((salesCnt.getQuantity() + quantity));
		    salesCountRepository.save(salesCnt);
		}
	}
}

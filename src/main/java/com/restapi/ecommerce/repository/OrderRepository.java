package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query(value = "select order_id, order_date, "
			+ "order_status, billing_address_id, cart_id, payment_id, "
			+ "shipping_address_id, user_id from orders where billing_address_id = ?1",
		   nativeQuery = true)
	List<Order> findByBillingAddressId(Long addressId);
}

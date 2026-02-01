package com.restapi.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByShippingAddressAddressIdOrBillingAddressAddressId(Long addressId, Long addressId2);
	List<Order> findByUserUserIdOrderByOrderDateDesc(Long userId);
	Optional<Order> findById(Long id);
}

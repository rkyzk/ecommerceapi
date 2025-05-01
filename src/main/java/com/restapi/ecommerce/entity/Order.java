package com.restapi.ecommerce.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderId;
	private Instant orderDate;
	private String orderStatus;

	@OneToOne
	@JoinColumn(name="cart_id")
	private Cart cart;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@OneToOne
	@JoinColumn(name="payment_id")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name="address_id")
	private Address address;
}

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
import jakarta.validation.constraints.NotNull;
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
	@NotNull
	private Cart cart;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@OneToOne
	@JoinColumn(name="payment_id")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name="shipping_address_id")
	@NotNull
	private Address shippingAddress;

	@ManyToOne
	@JoinColumn(name="billing_address_id")
	private Address billingAddress;

	@OneToOne
	@JoinColumn(name="review_id")
	private Review review;

	public Order(Instant orderDate, Cart cart, User user, Payment payment,
			Address shippingAddress, Address billingAddress) {
		this.orderDate = orderDate;
		this.orderStatus = "placed";
		this.cart = cart;
		this.user = user;
		this.payment = payment;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
	}
}

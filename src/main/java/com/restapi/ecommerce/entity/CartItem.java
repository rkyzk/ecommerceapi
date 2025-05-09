package com.restapi.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;

	@Column(name="quantity")
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name="cart_id")
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Cart cart;

	public CartItem(Product product, Integer quantity, Cart cart) {
		this.product = product;
		this.quantity = quantity;
		this.cart = cart;
	}
}

package com.restapi.ecommerce.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name="user_id")
	private User user;

	@OneToMany(mappedBy="cart",
			cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
			orphanRemoval = true)
	@JsonIgnoreProperties(value="cart")
	private Set<CartItem> cartItems = new HashSet<>();

	private double totalPrice = 0.0;

	private Instant orderedAt;

	public Cart(User user) {
		this.user = user;
	}
}

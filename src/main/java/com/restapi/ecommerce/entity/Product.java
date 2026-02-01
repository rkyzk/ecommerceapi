package com.restapi.ecommerce.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Size(min=3, max=40)
	private String productName;
	
	@NotNull
	@Max(99999)
	@Min(0)
	private Integer quantity;

	@Digits(integer=10, fraction=0)
	@Min(0)
	private double price;
	
	private double discount;

	@Digits(integer=10, fraction=0)
	@Min(0)
	private double specialPrice;

	@NotNull
	private boolean featured = false;
	
	@Size(max=100)
	private String imagePath;

	private String imageName;

	private Instant createdAt;

	private Instant updatedAt;

	private Instant deletedAt;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@NotNull
	private Category category;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User user;

	@OneToMany(mappedBy="product",
			cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
			orphanRemoval = true)
	@JsonIgnore
	Set<CartItem> cartItems = new HashSet<>();
}
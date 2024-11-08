package com.restapi.ecommerce.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min=3, max=50)
	private String productName;
	
	@Max(99999)
	@Min(0)
	private Integer quantity;
	
	@Digits(integer=10, fraction=0)
	private double price;
	
	@Size(max=200)
	private String description;

	private Instant createdAt;

	private Instant updatedAt;

	private Instant deletedAt;
	
	@ManyToMany(mappedBy = "products")
	@JsonIgnore
	private Set<Category> categories = new HashSet<>();
}
package com.restapi.ecommerce.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
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
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@NotBlank
	@Size(min = 3)
	private String categoryName;

	@ManyToMany(mappedBy = "categories", cascade = CascadeType.MERGE)
	@JsonBackReference
	private Set<Product> products = new HashSet<>();
	
	public void addProducts(Product product) {
		product.getCategories().add(this);
		this.products.add(product);
	}
	
	public void removeProducts(Long productId) {
		Product product = this.products.stream().filter(
				prod -> prod.getProductId() == productId)
		        .findFirst().orElse(null);
		if (product != null) this.products.remove(product);		
	}
}

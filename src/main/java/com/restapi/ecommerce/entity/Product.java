package com.restapi.ecommerce.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long productId;

	@NotBlank
	@Size(min=3, max=40)
	private String productName;
	
	@NotNull
	@Max(99999)
	@Min(0)
	private Integer stock;
	
	@Digits(integer=10, fraction=0)
	@Min(1)
	private double price;
	
	@Size(max=200)
	private String description;
	
	@Size(max=30)
	private String image_name;

	private Instant createdAt;

	private Instant updatedAt;

	private Instant deletedAt;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "product_category",
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
    )
	private Set<Category> categories = new HashSet<>();

	public void addCategory(Category category) {
		category.getProducts().add(this);
	    this.categories.add(category);   
	}
	
	public void removeCategory(long categoryId) {
		Category category = this.categories.stream()
				.filter(elem -> elem.getCategoryId() == categoryId)
				.findFirst().orElse(null);
		if (category != null) this.categories.remove(category);		
	}
}
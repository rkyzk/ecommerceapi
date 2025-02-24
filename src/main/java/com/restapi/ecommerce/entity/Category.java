package com.restapi.ecommerce.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@Size(min = 3, message="Category name must include at least 3 characters")
	private String categoryName;

//	@ManyToMany(mappedBy = "categories")
//	@JsonIgnore
//	private Set<Product> products = new HashSet<>();
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Product> products = new HashSet<>();
}

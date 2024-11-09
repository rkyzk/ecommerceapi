package com.restapi.ecommerce.payload;

import java.util.Set;

import com.restapi.ecommerce.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long productId;
	private String productName;
	private Integer stock;
	private double price;
	private Set<Category> categories;
	private String description;
}

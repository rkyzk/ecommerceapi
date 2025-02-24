package com.restapi.ecommerce.payload;

import java.util.HashSet;
import java.util.Set;

import com.restapi.ecommerce.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	private long categoryId;
	private String categoryName;
	private Set<Product> products = new HashSet<>();
}

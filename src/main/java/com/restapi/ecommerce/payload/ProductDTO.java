package com.restapi.ecommerce.payload;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.restapi.ecommerce.entity.Category;
import com.restapi.ecommerce.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	@Autowired
	private ModelMapper modelMapper;
	
	private Long productId;
	private String productName;
	private Integer stock;
	private double price;
	private String image_name;
	private Set<Category> categories;
	private String description;
	
	public void addCategory(Category category) {
		category.getProducts().add(modelMapper.map(this, Product.class));
		this.categories.add(category);
	}
}

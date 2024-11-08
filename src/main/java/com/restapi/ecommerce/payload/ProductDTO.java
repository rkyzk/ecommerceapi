package com.restapi.ecommerce.payload;

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
	private String description;
}

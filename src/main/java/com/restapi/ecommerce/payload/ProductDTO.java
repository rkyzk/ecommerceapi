package com.restapi.ecommerce.payload;

import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

import com.restapi.ecommerce.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private Long id;
	private String productName;
	private Integer quantity;
	private double price;
	private double discount;
	private double specialPrice;
	private boolean featured;
	private String imageName;
	private String imagePath;
	private Category category;
	private Instant deletedAt;
	private MultipartFile imgFile;

}
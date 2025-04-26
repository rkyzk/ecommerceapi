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
//	@Autowired
//	private ModelMapper modelMapper;

	private Long id;
	private String productName;
	private Integer quantity;
	private double price;
	private double discount;
	private double specialPrice;
	private String imageName;
	private String imagePath;
	private Category category;
//	private Set<Category> categories;
	private String description;
	private Instant deletedAt;
//	@FileName(maxLength=30)
//	@FileType
//	@FileSize(maxSize = 819200)
	private MultipartFile imgFile;

//	public void addCategory(Category category) {
//		category.getProducts().add(modelMapper.map(this, Product.class));
//		this.categories.add(category);
//	}
}
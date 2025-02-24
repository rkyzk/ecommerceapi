package com.restapi.ecommerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.restapi.ecommerce.entity.Category;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.repository.CategoryRepository;
import com.restapi.ecommerce.repository.ProductRepository;


@Configuration
public class DataInitializer {
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	
	public DataInitializer(CategoryRepository categoryRepository,
			ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	// executed after application starts when application context
	// is initialized
	@Bean
	public CommandLineRunner initializeData() {
		return (args -> {
			// create 3 categories
			Category category1 = new Category();
			Category category2 = new Category();
			Category category3 = new Category();
			
			category1.setCategoryName("cat1");
			category2.setCategoryName("cat2");
			category3.setCategoryName("cat3");
			categoryRepository.save(category1);
			categoryRepository.save(category2);
			categoryRepository.save(category3);
			
			// create 5 products
			Product product1 = new Product();
			product1.setProductName("product1");
			product1.setQuantity(50);
			product1.setPrice(1500);
			product1.setCategory(category1);
			product1.setCategory(category3);
			productRepository.save(product1);
			
			Product product2 = new Product();
			product2.setProductName("product2");
			product2.setQuantity(30);
			product2.setPrice(1200);
			product2.setCategory(category1);
			productRepository.save(product2);
			
			Product product3 = new Product();
			product3.setProductName("product3");
			product3.setQuantity(25);
			product3.setPrice(1600);
			product3.setCategory(category2);
			product3.setCategory(category3);
			productRepository.save(product3);
			
			Product product4 = new Product();
			product4.setProductName("product4");
			product4.setQuantity(40);
			product4.setPrice(1400);
			product4.setCategory(category1);
			productRepository.save(product4);
			
			Product product5 = new Product();
			product5.setProductName("product5");
			product5.setQuantity(20);
			product5.setPrice(1100);
			product5.setCategory(category2);
			productRepository.save(product5);
		
			// check FETCH TYPE
			// System.out.println("Fetching users");
			// userRepository.findById(1L);
		});
	}

}
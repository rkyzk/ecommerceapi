package com.restapi.ecommerce.config;

//@Configuration
//public class DataInitializer {
//	private final CategoryRepository categoryRepository;
//	private final ProductRepository productRepository;
//
//	public DataInitializer(CategoryRepository categoryRepository,
//			ProductRepository productRepository) {
//		this.categoryRepository = categoryRepository;
//		this.productRepository = productRepository;
//	}
//
//	// executed after application starts when application context
//	// is initialized
//	@Bean
//	public CommandLineRunner initializeData() {
//		return (args -> {
//			// create 3 categories
//			Category category1 = new Category();
//			Category category2 = new Category();
//			Category category3 = new Category();
//
//			category1.setCategoryName("tulips");
//			category2.setCategoryName("hyacinth");
//			category3.setCategoryName("croccus");
//			categoryRepository.save(category1);
//			categoryRepository.save(category2);
//			categoryRepository.save(category3);
//
//			// create 5 products
//			Product product1 = new Product();
//			product1.setProductName("viridiflora");
//			product1.setQuantity(50);
//			product1.setPrice(1500.00);
//			product1.setSpecialPrice(1400.00);
//			product1.setCategory(category1);
//			product1.setFeatured(true);
//			productRepository.save(product1);
//
//			Product product2 = new Product();
//			product2.setProductName("whittallii");
//			product2.setQuantity(30);
//			product2.setPrice(1200);
//			product2.setSpecialPrice(1000.00);
//			product2.setCategory(category1);
//			product2.setFeatured(true);
//			productRepository.save(product2);
//
//			Product product3 = new Product();
//			product3.setProductName("Anastasia");
//			product3.setQuantity(25);
//			product3.setPrice(1600);
//			product3.setSpecialPrice(1400.00);
//			product3.setCategory(category2);
//			productRepository.save(product3);
//
//			Product product4 = new Product();
//			product4.setProductName("advance crocus");
//			product4.setQuantity(40);
//			product4.setPrice(1400.00);
//			product4.setSpecialPrice(1200.00);
//			product4.setCategory(category3);
//			product4.setFeatured(true);
//			productRepository.save(product4);
//
//			Product product5 = new Product();
//			product5.setProductName("jeanne d'arc");
//			product5.setQuantity(20);
//			product5.setPrice(1100.00);
//			product5.setSpecialPrice(1000.00);
//			product5.setCategory(category3);
//			product5.setFeatured(true);
//			productRepository.save(product5);
//			
//			Product product6 = new Product();
//			product6.setProductName("queen of night");
//			product6.setQuantity(20);
//			product6.setPrice(1100.00);
//			product6.setSpecialPrice(1000.00);
//			product6.setCategory(category1);
//			productRepository.save(product6);
//
//			Set<Product> products1 = new HashSet<>();
//			products1.add(product1);
//			products1.add(product4);
//			products1.add(product6);
//			category1.setProducts(products1);
//
//			Set<Product> products2 = new HashSet<>();
//			products2.add(product3);
//			products2.add(product5);
//			category2.setProducts(products2);
//
//			Set<Product> products3 = new HashSet<>();
//			products3.add(product2);
//			category3.setProducts(products3);
//
//			categoryRepository.save(category1);
//			categoryRepository.save(category2);
//			categoryRepository.save(category3);
//		});
//	}
//}
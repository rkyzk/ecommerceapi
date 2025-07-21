package com.restapi.ecommerce.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.restapi.ecommerce.entity.Category;
import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.repository.CategoryRepository;
import com.restapi.ecommerce.repository.ProductRepository;

/** product service implementation */
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ImgUploadService imgUploadService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * get product data.
	 * Filter by keywords and category
	 * if the parameters are present.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param keywords
	 * @param categoryId
	 * 
	 * @return product data
	 */
	@Override
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder, String keywords, String categoryId) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage = null;
		if (keywords == null || keywords.isEmpty()) {
			if (categoryId == null || categoryId.isEmpty()) {
			    // if both category and keywords aren't specified
                productPage = productRepository.findByDeletedAtIsNull(pageDetails);
		    } else {
		    	// if category alone is specified
		    	productPage = productRepository
		    			.findByCategoryCategoryIdAndDeletedAtIsNull(
		    					Long.valueOf(categoryId), pageDetails);
		    }
		} else {
			// get three keywords
			String[] kwArr = keywords.split("_");
			String keyword = "";
			String keyword2 = "";
		    String keyword3 = "";
	    	keyword = kwArr[0];
			if (kwArr.length > 1) keyword2 = kwArr[1];
			if (kwArr.length > 2) keyword3 = kwArr[2];
			if (categoryId == null || categoryId.isEmpty()) {
		        // only keywords are specified
				productPage = productRepository
	                    .findProductsByKeywords(keyword, keyword2, keyword3, pageDetails);
			} else {
				// both keywords and category are speicfied
				productPage = productRepository
						.findProductsByKeywordsAndCategory(
								keyword, keyword2, keyword3, Long.valueOf(categoryId), pageDetails);
			}
		}
		List<Product> products = productPage.getContent();
		if (products.isEmpty()) {
			throw new APIException("No products present");
		}
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse response = new ProductResponse();
		response.setContent(productDTOs);
		// set pagination data
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	};

	/**
	 * get products by category
	 */
	@Override
	public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage = productRepository.findByCategoryCategoryIdAndDeletedAtIsNull(categoryId, pageDetails);
		List<Product> products = productPage.getContent();
		if (products.isEmpty()) {
			throw new APIException("No products present");
		}
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse response = new ProductResponse();
		response.setContent(productDTOs);
		// set pagination data
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;	
	}

	/**
	 * add a new product
	 */
	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Category", "categoryId", categoryId));
		Product product = productRepository.findByProductName(productDTO.getProductName());
		if (product != null)
			throw new APIException("Product with the given name exists");
		productDTO.setCategory(category);
		double specialPrice = productDTO.getPrice() * (1 - productDTO.getDiscount() * 0.01);
		productDTO.setSpecialPrice(specialPrice);
		MultipartFile file = productDTO.getImgFile();
		// if image was added:
		if (file != null && !file.isEmpty()) {
			String imageName = productDTO.getImgFile().getOriginalFilename();
			// store it in S3 bucket
			String imagePath = uploadImage(imageName, file, category.getCategoryName());
			productDTO.setImageName(imageName);
		    productDTO.setImagePath(imagePath);
		}
		Product prodData = modelMapper.map(productDTO, Product.class);
		Product savedProduct = productRepository.save(prodData);
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	/**
	 * update product
	 *
	 */
	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);
		Product productToUpdate = storedProduct
				.orElseThrow(()
						-> new ResourceNotFoundException("Product", "productId", prodId));
		MultipartFile file = productDTO.getImgFile();
		// if image was added:
		if (file != null && !file.isEmpty()) {
			String imageName = productDTO.getImgFile().getOriginalFilename();
			// store it in S3 bucket
			String imagePath = uploadImage(imageName, file,
					productDTO.getCategory().getCategoryName());
			productDTO.setImageName(imageName);
		    productDTO.setImagePath(imagePath);
		}
	    productToUpdate.setProductName(productDTO.getProductName());
	    productToUpdate.setQuantity(productDTO.getQuantity());
	    productToUpdate.setPrice(productDTO.getPrice());
	    productToUpdate.setCategory(productDTO.getCategory());
		Product updatedProduct = productRepository.save(productToUpdate);
		ProductDTO updatedProdDTO = modelMapper.map(updatedProduct, ProductDTO.class);
		return updatedProdDTO;
	}

	/**
	 * delete product
	 */
	@Override
	public ProductDTO deleteProduct(Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);	
		Product productToDelete = storedProduct
				.orElseThrow(()
						-> new ResourceNotFoundException("Product", "productId", prodId));
		String imageName = productToDelete.getImageName();
		// if there's an image file, delete it from S3 bucket. 
		if ((imageName != "") && (imageName != null)) {
			imgUploadService.deleteImg(imageName);
		}
	    productToDelete.setDeletedAt(Instant.now());
		Product deletedProd = productRepository.save(productToDelete);
		ProductDTO deletedProdDTO = modelMapper.map(deletedProd, ProductDTO.class);
		return deletedProdDTO;
	}

	/**
	 * get featured products
	 */
	@Override
	public List<ProductDTO> getFeaturedProducts() {	
		List<Product> products =
				productRepository.findByFeaturedIsTrue();
		if (products.isEmpty()) {
			throw new APIException("No featured products present");
		}
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		return productDTOs;
	}

	/**
	 * Upload image on S3 Bucket.
	 * 
	 * @param imageName
	 * @param file
	 * @param categoryName
	 * @return image path
	 */
	private String uploadImage(String imageName, MultipartFile file, String categoryName) {
		// store it in S3 bucket
		String imagePath = imgUploadService.uploadImg(
			file, categoryName, // specify the folder 
			imageName);
		// if upload fails, set error response
		if (imagePath == null) { 
			// to do
		}
		return imagePath;
	}
}
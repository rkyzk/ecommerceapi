package com.restapi.ecommerce.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * product service implementation
 */
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ImageUploadService imgUploadService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${msg.product.service001}")
	private String msg001;

	@Value("${msg.product.service002}")
	private String msg002;

	@Value("${msg.product.service003}")
	private String msg003;

	/**
	 * Get products data, filtered and sorted if specified.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param keywords
	 * @param categoryId
	 * @param colors
	 *
	 * @return products data
	 */
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder, String keywords, String categoryId, String colors) {
		List<String> keywordList = null;
		String colorStr = null;
		// get keywords list if any
		if (!StringUtils.isEmpty(keywords)) keywordList = getKeywordList(keywords);
		// get color string if any
		if (!StringUtils.isEmpty(colors)) colorStr = getColors(colors);
		List<Product> products = null;
		if (sortBy.equals("sales_count")) {
			// if the result should be sorted by sales count
			products = productRepository.getProductsSortBySalesCount(pageNumber, pageSize,
					categoryId, keywordList, colorStr);
		} else {
			products = productRepository.getProducts(pageNumber, pageSize, sortBy, sortOrder,
					categoryId, keywordList, colorStr);
		}
		if (products.isEmpty()) {
			throw new APIException(msg001); // "No products found."
		}
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse response = new ProductResponse();
		response.setContent(productDTOs);
		Long totalElements = productRepository.getTotalElements(categoryId, keywordList, colorStr);
		// calculate total pages
		Double doubleVal = Math.ceil((double)totalElements / (double)pageSize);
		Integer totalPages = doubleVal.intValue();
		// set pagination data
		response.setPageNumber(pageNumber);
		response.setPageSize(pageSize);
		response.setTotalElements(totalElements);
		response.setTotalPages(totalPages);
		response.setLastPage(pageNumber == totalPages - 1);
		return response;
	}

	/**
	 * Return list of keywords
	 *
	 * @param keywords
	 * @return
	 */
	private List<String> getKeywordList(String keywords) {
		return Arrays.asList(keywords.split("_"));
	}

	/**
	 * Return list of keywords
	 *
	 * @param keywords
	 * @return
	 */
	private String getColors(String colors) {
        return "(" + colors.replace("_", ",") + ")";
	}

	/**
	 * Get products data filtered by category
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param categoryId
	 * 
	 * @return products data
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
			throw new APIException(msg001); // "No products found."
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
	 * Add a new product
	 */
	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Category", "categoryId", categoryId));
		Product product = productRepository.findByProductName(productDTO.getProductName());
		if (product != null)
			throw new APIException(msg002); // "The product name is already used."
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
			//imgUploadService.deleteImage(imageName);
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
			throw new APIException(msg003); // "No featured products present"
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
//		String imagePath = imgUploadService.uploadImage(
//			file, categoryName, // specify the folder 
//			imageName);
		// if upload fails, set error response
//		if (imagePath == null) { 
//			// to do
//		}
		return null;
	}
}
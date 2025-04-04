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

	@Override
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage = productRepository.findByDeletedAtIsNull(pageDetails);
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

	@Override
	public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage = productRepository.findByCategoryCategoryId(categoryId, pageDetails);
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
	    productToUpdate.setDescription(productDTO.getDescription());
	    productToUpdate.setCategory(productDTO.getCategory());
		Product updatedProduct = productRepository.save(productToUpdate);
		ProductDTO updatedProdDTO = modelMapper.map(updatedProduct, ProductDTO.class);
		return updatedProdDTO;
	}

	@Override
	public ProductDTO deleteProduct(Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);	
		Product productToDelete = storedProduct
				.orElseThrow(()
						-> new ResourceNotFoundException("Product", "productId", prodId));
		String imageName = productToDelete.getImageName();
		// if there's an image file, delete it from S3 bucket. 
		if (!(imageName == "") || !(imageName == null)) {
			imgUploadService.deleteImg(imageName);
		}
	    productToDelete.setDeletedAt(Instant.now());
		Product deletedProd = productRepository.save(productToDelete);
		ProductDTO deletedProdDTO = modelMapper.map(deletedProd, ProductDTO.class);
		return deletedProdDTO;
	}

	@Override
	public ProductResponse searchProductsByKeyword(String keyword,
			Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage =
				productRepository.findByProductNameContainingIgnoreCase(keyword, pageDetails);
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
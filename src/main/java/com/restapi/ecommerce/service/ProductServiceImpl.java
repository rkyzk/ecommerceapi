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
	 * 商品データを取得し返却する。
	 * カテゴリー、色、キーワード指定があるときは
	 * フィルターしてデータを返却する。
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param keywords
	 * @param categoryId
	 * @param colors
	 * 
	 * @return product data
	 */
	@Override
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder, String keywords, String categoryId, String colors) {
		// ソート順を設定
		Sort sortByAndOrder;
		if (sortBy.equals("sales_count")) {
			sortByAndOrder = Sort.unsorted(); // 売れ行き順の場合はSort設定せずクエリで並べ替えを実施
		} else {
			sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
					? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();
		}
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPage = null;
		if ((colors == null || colors.isEmpty()) &&
			(keywords == null || keywords.isEmpty())) {
			if (categoryId == null || categoryId.isEmpty()) {
				if (sortBy.equals("sales_count")) {
					// 絞り込みなし、売れ行き順降順
					productPage = productRepository.findByDeletedAtIsNullSortBySalesCount(pageDetails);
				} else {
					// 絞り込みなし、product_id昇順
					productPage = productRepository.findByDeletedAtIsNull(pageDetails);
				}
			} else {
				if (sortBy.equals("sales_count")) {
					// カテゴリーでフィルター
			    	productPage = productRepository
			    			.findByCategoryCategoryIdAndDeletedAtIsNullSortBySalesCount(
			    					Long.valueOf(categoryId), pageDetails);
				} else {
					// カテゴリーでフィルター、product_id昇順
					productPage = productRepository
			    			.findByCategoryCategoryIdAndDeletedAtIsNull(
			    					Long.valueOf(categoryId), pageDetails);
				}
			}
		} else {
			// カテゴリー、色、キーワードでフィルター
			// キーワード設定
			String keyword = "";
			String keyword2 = "";
			String keyword3 = "";
			if (keywords != null && !keywords.isEmpty()) {
				String[] kwArr = keywords.split("_");
				keyword = kwArr[0];
				if (kwArr.length > 1) keyword2 = kwArr[1];
				if (kwArr.length > 2) keyword3 = kwArr[2];
			}
			// カテゴリー設定
	    	Long categoryIdMin = (long)0;
	    	Long categoryIdMax = (long)100;
	    	if (categoryId != null && !categoryId.isEmpty()) {
	    		// カテゴリー指定されているときカテゴリーIdを設定
	    		categoryIdMin = Long.valueOf(categoryId);
	    		categoryIdMax = Long.valueOf(categoryId);
	    	}
	    	// 色設定
	    	Long colorId1 = (long)1;
    		Long colorId2 = (long)2;
    		Long colorId3 = (long)3;
    		Long colorId4 = (long)4;
    		Long colorId5 = (long)5;
    		Long colorId6 = (long)6;
    		Long colorId7 = (long)7;
    		Long colorId8 = (long)8;
	    	if (colors != null && !colors.isEmpty()) {
		    	// 色指定を設定
				String[] colorArr = colors.split("_");
				colorId1 = Long.valueOf(colorArr[0]);
				colorId2 = colorArr.length > 1 ? Long.valueOf(colorArr[1]) : (long)0;
				colorId3 = colorArr.length > 2 ? Long.valueOf(colorArr[2]) : (long)0;
				colorId4 = colorArr.length > 3 ? Long.valueOf(colorArr[3]) : (long)0;
				colorId5 = colorArr.length > 4 ? Long.valueOf(colorArr[4]) : (long)0;
				colorId6 = colorArr.length > 5 ? Long.valueOf(colorArr[5]) : (long)0;
				colorId7 = colorArr.length > 6 ? Long.valueOf(colorArr[6]) : (long)0;
				colorId8 = colorArr.length > 7 ? Long.valueOf(colorArr[7]) : (long)0;
	    	}
	    	if (sortBy.equals("sales_count")) {
	    		// カテゴリー、色、キーワードでフィルター、売れ行き降順
	    		productPage = productRepository
						.findProductsByKeywordsAndCategoryAndColorsSortBySalesCount(
								keyword, keyword2, keyword3, categoryIdMin, categoryIdMax,
								colorId1, colorId2, colorId3, colorId4, colorId5, colorId6,
								colorId7, colorId8, pageDetails);
	    	} else {
	    		// カテゴリー、色、キーワードでフィルター、product_id昇順
				productPage = productRepository
					.findProductsByKeywordsAndCategoryAndColors(
							keyword, keyword2, keyword3, categoryIdMin, categoryIdMax,
							colorId1, colorId2, colorId3, colorId4, colorId5, colorId6,
							colorId7, colorId8, pageDetails);
	    	}
		}
		List<Product> products = productPage.getContent();
		if (products.isEmpty()) {
			throw new APIException("商品がありません");
		}
		List<ProductDTO> productDTOs = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse response = new ProductResponse();
		response.setContent(productDTOs);
		// パジネーションデータ設定
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	};

	/**
	 * カテゴリーでフィルターした商品データを取得し返却する。
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param categoryId
	 * 
	 * @return product data
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
			throw new APIException("商品がありません");
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
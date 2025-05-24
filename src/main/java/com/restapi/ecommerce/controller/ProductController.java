package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.config.AppConstants;
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;

	/**
	 * get all products
	 *
	 * @param keywords
	 * @param categoryId
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getProducts(
			@RequestParam (name = "keywords", required=false) String keywords,
			@RequestParam (name = "category", required=false) String categoryId,
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ProductResponse response = productService.getProducts(pageNumber, pageSize, sortBy,
				                                              sortOrder, keywords, categoryId);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	/**
	 * get featured products
	 *
	 * @return
	 */
	@GetMapping("/public/products/featured")
	public ResponseEntity<List<ProductDTO>> getFeaturedProducts() {
		List<ProductDTO> products = productService.getFeaturedProducts();
		return new ResponseEntity<List<ProductDTO>> (products, HttpStatus.OK);
	}

	/**
	 * get products by keywords (not used at the moment)
	 * (for search, use instead getProducts with parameter 'keywords ')
	 *
	 * @param keywords
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
//	@GetMapping("/public/products/keywords/{keywords}")
//	public ResponseEntity<ProductResponse> searchProductsByKeyword(@PathVariable String keywords,
//			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
//			    required=false) Integer pageNumber,
//			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
//			    required=false) Integer pageSize,
//			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
//			    required=false) String sortBy,
//			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
//			    required=false) String sortOrder) {
//		ProductResponse response = productService.searchProductsByKeywords(keywords, pageNumber,
//				pageSize, sortBy, sortOrder);
//		return new ResponseEntity<> (response, HttpStatus.OK);
//	}

	/**
	 * get products by category
	 *
	 * @param categoryId
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ProductResponse response = productService.getProductsByCategory(categoryId, pageNumber,
				pageSize, sortBy, sortOrder);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

    /**
     * add product
     *
     * @param productDTO
     * @param categoryId
     * @return
     */
	@PostMapping("/admin/category/{categoryId}/product")
	public ResponseEntity<ProductDTO> postProduct(@Valid @RequestBody ProductDTO productDTO,
			@PathVariable Long categoryId) {
		ProductDTO savedProduct = productService.addProduct(categoryId, productDTO);
		return new ResponseEntity<> (savedProduct, HttpStatus.CREATED);
	}

	/**
	 * update product
	 *
	 * @param productDTO
	 * @param prodId
	 * @return
	 */
	@PutMapping("/admin/products/{prodId}")
	public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
			@PathVariable Long prodId) {
		ProductDTO updatedProdDTO = productService.updateProduct(productDTO, prodId);
		return new ResponseEntity<> (updatedProdDTO, HttpStatus.OK);
	}

	/**
	 * delete product
	 *
	 * @param prodId
	 * @return
	 */
	@PutMapping("/admin/products/delete/{prodId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long prodId) {
		ProductDTO productDTO = productService.deleteProduct(prodId);
	    return new ResponseEntity<> (productDTO, HttpStatus.OK);
	}
}
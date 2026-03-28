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
import com.restapi.ecommerce.entity.ProductDetail;
import com.restapi.ecommerce.payload.APIResponse;
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.service.ProductDetailService;
import com.restapi.ecommerce.service.ProductService;

import jakarta.validation.Valid;

/**
 * Controller that handles request for
 * selecting, inserting, updating, deleting product data.
 * 
 * @author reikoyazaki
 *
 */
@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailService productDetailService;

	/**
	 * Get product list
	 * filtered and sorted according to given parameters
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
	public ResponseEntity<?> getProducts(
			@RequestParam (name = "keywords", required=false) String keywords,
			@RequestParam (name = "category", required=false) String categoryId,
			@RequestParam (name = "colors", required=false) String colors,
			@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
			    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ProductResponse response = productService.getProducts(pageNumber, pageSize, sortBy,
				sortOrder, keywords, categoryId, colors);
		if (response == null) {
			APIResponse resp = new APIResponse();
			resp.setMessage("No products found.");
			resp.setStatus(false);
			return new ResponseEntity<> (resp, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	/**
	 * Return list of featured products.
	 *
	 * @return list of featured products
	 */
	@GetMapping("/public/products/featured")
	public ResponseEntity<List<ProductDTO>> getFeaturedProducts() {
		List<ProductDTO> products = productService.getFeaturedProducts();
		return new ResponseEntity<List<ProductDTO>> (products, HttpStatus.OK);
	}

	/**
	 * Get product details
	 *
	 * @param product id
	 * @return ResponseEntity
	 */
	@GetMapping("/public/product/detail/{productId}")
	public ResponseEntity<List<ProductDetail>> getProductDetail(@PathVariable Long productId) {
		List<ProductDetail> productDetail = productDetailService.getProductDetail(productId);
		return new ResponseEntity<List<ProductDetail>> (productDetail, HttpStatus.OK);
	}

	/**
	 * Get product list of a specific category
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
     * Add product
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
	 * Update product
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
	 * Delete product
	 * (Insert deleted date in column 'deleted_at')
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
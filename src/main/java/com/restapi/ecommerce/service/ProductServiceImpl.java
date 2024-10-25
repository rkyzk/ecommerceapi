package com.restapi.ecommerce.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.ProductDTO;
import com.restapi.ecommerce.payload.ProductResponse;
import com.restapi.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize) {
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
		Page<Product> productPage = productRepository.findAll(pageDetails);
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
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = modelMapper.map(productDTO, Product.class);
		Product savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDTO.class);
	}
	
	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, Long prodId) {
		Optional<Product> storedProduct = productRepository.findById(prodId);
		Product productToUpdate = storedProduct
				.orElseThrow(()
						-> new ResourceNotFoundException("Product", "productId", prodId));
	    productToUpdate.setName(productDTO.getName());
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
	    productToDelete.setDeletedAt(Instant.now());
		Product deletedProd = productRepository.save(productToDelete);
		ProductDTO deletedProdDTO = modelMapper.map(deletedProd, ProductDTO.class);
		return deletedProdDTO;
	}
}

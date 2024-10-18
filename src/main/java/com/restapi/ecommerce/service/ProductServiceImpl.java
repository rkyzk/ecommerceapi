package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.entity.Product;

public class ProductServiceImpl implements ProductService {
	public List<Product> getProducts() {
		return productRepository.findAll();
	};

}

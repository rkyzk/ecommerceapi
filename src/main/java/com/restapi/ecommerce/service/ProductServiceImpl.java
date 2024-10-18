package com.restapi.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Product;
import com.restapi.ecommerce.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<Product> getProducts() {
		return productRepository.findAll();
	};
}

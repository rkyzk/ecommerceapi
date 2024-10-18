package com.restapi.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Product;

@Service
public interface ProductService {
	public List<Product> getProducts();
}

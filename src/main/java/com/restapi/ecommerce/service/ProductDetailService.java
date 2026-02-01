package com.restapi.ecommerce.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ProductDetailService {
	Map<String, String> getProductDetail(Long productId);
}

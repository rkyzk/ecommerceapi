package com.restapi.ecommerce.service;

import java.util.Map;

public interface ProductDetailService {
	Map<String, String> getProductDetail(Long productId);
}

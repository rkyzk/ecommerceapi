package com.restapi.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.ProductDetail;
import com.restapi.ecommerce.repository.ProductDetailRepository;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
	ProductDetailRepository productDetailRepository;

	@Override
	public Map<String, String> getProductDetail(Long productId) {
		List<ProductDetail> prodDetailList = productDetailRepository
				.findByProductIdOrderByIdAsc(productId);
		Map<String, String> map = new HashMap<>();
		prodDetailList.forEach(item -> {
            map.put(item.getKey(), item.getValue());			
		});
		return map;
	}
}

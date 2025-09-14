package com.restapi.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.ProductDetail;
import com.restapi.ecommerce.repository.ProductDetailRepository;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
	ProductDetailRepository productDetailRepository;

	@Override
	public List<ProductDetail> getProductDetail(Long productId) {
		return productDetailRepository
				.findByProductIdOrderByIdAsc(productId);
	}
}


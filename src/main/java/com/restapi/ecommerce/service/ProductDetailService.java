package com.restapi.ecommerce.service;

import java.util.List;
import com.restapi.ecommerce.entity.ProductDetail;

public interface ProductDetailService {
	List<ProductDetail> getProductDetail(Long productId);
}

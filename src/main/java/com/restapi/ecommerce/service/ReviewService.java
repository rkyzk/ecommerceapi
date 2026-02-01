package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.ReviewDTO;
import com.restapi.ecommerce.payload.ReviewResponse;

/** review service interface */
public interface ReviewService {
	public ReviewResponse getAllReviews(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public Long postReview(ReviewDTO reviewDTO, Long orderId);
}
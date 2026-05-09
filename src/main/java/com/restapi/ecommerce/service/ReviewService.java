package com.restapi.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import com.restapi.ecommerce.payload.ReviewResponse;

/** review service interface */
public interface ReviewService {
	public ReviewResponse getAllReviews(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);
	public Long postReview(String reviewContent, byte stars,
			String displayName, MultipartFile file, Long orderId);
}
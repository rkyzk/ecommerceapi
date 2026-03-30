package com.restapi.ecommerce.service;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.Review;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.ReviewDTO;
import com.restapi.ecommerce.payload.ReviewResponse;
import com.restapi.ecommerce.repository.OrderRepository;
import com.restapi.ecommerce.repository.ReviewRepository;
import com.restapi.ecommerce.utils.AuthUtil;

/** product service implementation */
@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ImgUploadService imgUploadService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	AuthUtil authUtil;

	/**
	 * Get all reviews
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * 
	 * @return list of reviews
	 */
	@Override
	public ReviewResponse getAllReviews(Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		Page<Review> reviewPage = null;
		// get all reviews
		reviewPage = reviewRepository.findByValidIsTrueOrderByCreatedAtDesc(pageDetails);
		List<Review> reviews = reviewPage.getContent();
		// return null if no reviews are present
		if (reviews.isEmpty()) return null;
		List<ReviewDTO> reviewDTOs = reviews.stream()
				.map(review -> modelMapper.map(review, ReviewDTO.class))
				.toList();
		ReviewResponse response = new ReviewResponse();
		response.setContent(reviewDTOs);
		// set pagination data
		response.setPageNumber(reviewPage.getNumber());
		response.setPageSize(reviewPage.getSize());
		response.setTotalElements(reviewPage.getTotalElements());
		response.setTotalPages(reviewPage.getTotalPages());
		response.setLastPage(reviewPage.isLast());
		return response;
	};

	@Override
	public Long postReview(ReviewDTO reviewDTO, Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
		reviewDTO.setOrder(order);
		reviewDTO.setUser(authUtil.loggedinUser());
		reviewDTO.setCreatedAt(Instant.now());
		reviewDTO.setValid(true);
		Review savedReview = reviewRepository.save(modelMapper.map(reviewDTO, Review.class));
		// set review instance to the corresponding order instance
		order.setReview(savedReview);
		orderRepository.save(order);
		return savedReview.getId();
	}
}
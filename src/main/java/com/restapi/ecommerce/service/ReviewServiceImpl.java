package com.restapi.ecommerce.service;


import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	private ImageUploadService imgUploadService;

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
		reviewPage = reviewRepository.findByPublicizeFlgIsTrueOrderByCreatedAtDesc(pageDetails);
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
	public Long postReview(String reviewContent, Byte stars,
			String displayName, MultipartFile file, Long orderId) {
		ReviewDTO reviewDTO = new ReviewDTO();
		reviewDTO.setReviewContent(reviewContent);
		reviewDTO.setStars(stars);
		reviewDTO.setDisplayName(displayName);
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
		reviewDTO.setOrder(order);
		reviewDTO.setUser(authUtil.loggedinUser());
		reviewDTO.setCreatedAt(Instant.now());
		// if image was added, upload it on S3 
		if (file != null && !file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			int ind = fileName.lastIndexOf(".");
			String updatedFileName = fileName.substring(0, ind) + getSaltString(12) + 
					"." + fileName.substring(ind + 1);
			// get current year
			String year = ((Integer)Calendar.getInstance().get(Calendar.YEAR)).toString();
			// upload the image to S3 bucket
			try {
				imgUploadService.uploadImage(year + "/" + updatedFileName, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    reviewDTO.setImagePath(year + "/" + updatedFileName);
		}
		Review savedReview = reviewRepository.save(modelMapper.map(reviewDTO, Review.class));
		// set review instance to the corresponding order instance
		order.setReview(savedReview);
		orderRepository.save(order);
		return savedReview.getId();
	}

	private String getSaltString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
package com.restapi.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.config.AppConstants;
import com.restapi.ecommerce.payload.APIResponse;
import com.restapi.ecommerce.payload.ReviewDTO;
import com.restapi.ecommerce.payload.ReviewResponse;
import com.restapi.ecommerce.service.ReviewService;

/**
 * 
 * @author reikoyazaki
 *
 */
@RestController
@RequestMapping("/api")
public class ReviewController {
	@Autowired
	ReviewService reviewService;

	/**
	 * Get list of review entries.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	@GetMapping("/public/reviews")
	public ResponseEntity<?> getReviews(@RequestParam (name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,
		    required=false) Integer pageNumber,
			@RequestParam (name = "pageSize", defaultValue = "10",
			    required=false) Integer pageSize,
			@RequestParam (name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,
			    required=false) String sortBy,
			@RequestParam (name = "sortOrder", defaultValue = AppConstants.SORT_DIR,
			    required=false) String sortOrder) {
		ReviewResponse reviewResponse = reviewService.getAllReviews(pageNumber, pageSize,
				sortBy, sortOrder);
		if (reviewResponse == null) {
			APIResponse response = new APIResponse();
			response.setMessage("No reivews available.");
			return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ReviewResponse>(reviewResponse, HttpStatus.OK);
	}

	/**
	 * Save a review entry for an order.
	 *
	 * @param reviewDTO
	 * @param orderId
	 * @return
	 */
	@PostMapping("/review/{orderId}")
	public ResponseEntity<?> postReview(@RequestBody ReviewDTO reviewDTO, @PathVariable Long orderId) {
		Long reviewId = reviewService.postReview(reviewDTO, orderId);
		APIResponse response = new APIResponse();
		response.setMessage("The review entry has been saved." + reviewId);
		return new ResponseEntity<APIResponse> (response, HttpStatus.OK);
	}
}

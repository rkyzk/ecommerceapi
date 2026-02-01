package com.restapi.ecommerce.payload;

import java.time.Instant;

import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	private Long id;
	private String reviewContent;
	private User user;
	private Order order;
	private byte stars;
	private Instant createdAt;
	private Instant updatedAt;
	private boolean anonym;
	private boolean valid;
//	@FileName(maxLength=30)
//	@FileType
//	@FileSize(maxSize = 819200)
//	private MultipartFile imgFile;
}
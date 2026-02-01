package com.restapi.ecommerce.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
	private List<ReviewDTO> content;
	private Integer PageNumber;
	private Integer PageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;	
}

package com.restapi.ecommerce.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	Page<Review> findByValidIsTrueOrderByCreatedAtDesc(Pageable pageDetails);
}
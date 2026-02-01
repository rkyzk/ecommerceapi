package com.restapi.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	RefreshToken findByUserUserId(Long id);

	Optional<RefreshToken> findByRefreshToken(String token);
	int deleteByUserUserId(long userId);
}
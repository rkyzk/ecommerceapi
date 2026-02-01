package com.restapi.ecommerce.security.jwt.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.RefreshToken;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.APIException;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.repository.RefreshTokenRepository;
import com.restapi.ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

    @Value("${jwt.refreshtokenExpirationMs}")
    private long refreshTokenExp;

    public Optional<RefreshToken> getToken(String token) {
    	return refreshTokenRepository.findByRefreshToken(token);
    }

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		User user = userRepository.findByUserId(userId);
		refreshToken.setUser(user);
		refreshToken.setRefreshTokenExpTime(Instant.now().plusMillis(refreshTokenExp));
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
		return savedToken;
	}

	public boolean verifyRefreshTokenExp(String token) throws APIException {
		RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Token", "token value", token));
		if (refreshToken.getRefreshTokenExpTime().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(refreshToken);
			// throw new APIException("Token has expired. Please sign in again.");
			return false;
		}
		return true;
	}

	@Transactional
	public int deleteToken(long userId) {
		return refreshTokenRepository.deleteByUserUserId(userId);
	}
}

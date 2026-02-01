package com.restapi.ecommerce.security.jwt.service;

import com.restapi.ecommerce.entity.RefreshToken;
import com.restapi.ecommerce.entity.User;

public interface RefreshTokenService {
	RefreshToken getRefreshToken(User user);
}

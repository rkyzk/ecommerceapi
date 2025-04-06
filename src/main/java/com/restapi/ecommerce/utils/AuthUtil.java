package com.restapi.ecommerce.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.repository.UserRepository;

public class AuthUtil {
	@Autowired
	UserRepository userRepository;

	public User loggedinUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new UsernameNotFoundException(
						"User with the name " + authentication.getName() + " is not found."));
		return user;
	}
	

}

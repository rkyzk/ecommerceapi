package com.restapi.ecommerce.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.repository.UserRepository;

@Component
public class AuthUtil {
	@Autowired
	UserRepository userRepository;

	/** ログイン中ユーザのデータを取得 */
	public User loggedinUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new UsernameNotFoundException(
						"ユーザ名が「" + authentication.getName() + "」のユーザは見つかりません。"));
		return user;
	}
}

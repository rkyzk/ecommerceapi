package com.restapi.ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.security.jwt.JwtUtils;
import com.restapi.ecommerce.security.jwt.service.UserDetailsImpl;
import com.restapi.ecommerce.security.request.LoginRequest;
import com.restapi.ecommerce.security.response.UserInfoResponse;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginReq) {
		Authentication authentication;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							loginReq.getUsername(), loginReq.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object> (map, HttpStatus.NOT_FOUND);
		}
		// set authentication for the session
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserInfoResponse resp = new UserInfoResponse(userDetails.getId(),
				userDetails.getUsername(), roles, jwtToken);
		return ResponseEntity.ok(resp);
	}
}

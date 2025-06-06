package com.restapi.ecommerce.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.entity.Role;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.enums.AppRole;
import com.restapi.ecommerce.repository.RoleRepository;
import com.restapi.ecommerce.repository.UserRepository;
import com.restapi.ecommerce.security.jwt.JwtUtils;
import com.restapi.ecommerce.security.jwt.service.UserDetailsImpl;
import com.restapi.ecommerce.security.request.LoginRequest;
import com.restapi.ecommerce.security.request.SignUpRequest;
import com.restapi.ecommerce.security.response.MessageResponse;
import com.restapi.ecommerce.security.response.UserInfoResponse;

import jakarta.validation.Valid;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	/**
	 * authenticate user using given username and password.
	 * generate jwt Cookie from user details
	 * set jwt cookie to the header and return the body with
	 * user details
	 *
	 * @param loginReq
	 * @return
	 */
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
	    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserInfoResponse resp = new UserInfoResponse(userDetails.getId(),
				userDetails.getUsername(), roles);
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(resp);
	}

	/**
	 * check if the given username and email haven't been registered,
	 * and if not, set roles and save user
	 *
	 * @param req
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest req) {
		if (userRepository.existsByUsername(req.getUsername())) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Username is already used."));
		}
		if (userRepository.existsByEmail(req.getEmail())) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Email is already used."));
		}

		User user = new User(req.getUsername(),
				req.getEmail(),
				encoder.encode(req.getPassword()));

		Set<String> strRoles = req.getRoles();
		Set<Role> roles = new HashSet<Role>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch(role) {
					case "admin":
						Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
						roles.add(adminRole);
						break;
					case "seller":
						Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
						roles.add(sellerRole);
						break;
					default:
						Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Role is not found"));
						roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully"));
	}

	/**
	 * get and return logged in user's name
	 *
	 * @param authentication
	 * @return
	 */
	@GetMapping("/username")
	public String currentUsername(Authentication authentication) {
		if (authentication != null)
			return authentication.getName();
		else
			return "";
	}

	/**
	 * get and return logged in user's user details
	 * @param authentication
	 * @return
	 */
	@GetMapping("/user")
	public ResponseEntity<?> getUserDetails(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserInfoResponse resp = new UserInfoResponse(userDetails.getId(),
				userDetails.getUsername(), roles);
		return ResponseEntity.ok().body(resp);
	}

	/**
	 * sign out user.
	 *
	 * @param authentication
	 * @return
	 */
	@PostMapping("/signout")
	public ResponseEntity<?> signoutUser(Authentication authentication) {
		ResponseCookie cleanJwtCookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
				.body(new MessageResponse("You've been signed out."));
	}
}

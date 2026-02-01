package com.restapi.ecommerce.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.entity.RefreshToken;
import com.restapi.ecommerce.entity.Role;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.enums.AppRole;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.repository.RoleRepository;
import com.restapi.ecommerce.repository.UserRepository;
import com.restapi.ecommerce.security.jwt.JwtUtils;
import com.restapi.ecommerce.security.jwt.service.RefreshTokenServiceImpl;
import com.restapi.ecommerce.security.jwt.service.UserDetailsImpl;
import com.restapi.ecommerce.security.jwt.service.UserDetailsServiceImpl;
import com.restapi.ecommerce.security.request.LoginRequest;
import com.restapi.ecommerce.security.request.SignUpRequest;
import com.restapi.ecommerce.security.response.MessageResponse;
import com.restapi.ecommerce.security.response.UserInfoResponse;
import com.restapi.ecommerce.utils.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/api")
@RestController
public class AuthController {
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthUtil authUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RefreshTokenServiceImpl refreshTokenService;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	PasswordEncoder encoder;

	/**
	 * authenticate user using given username and password.
	 * generate jwt Cookie from user details
	 * generate refresh token
	 * set jwt cookie and refresh cookie to the header and return the body with
	 * user details
   *
	 * 入力されたユーザ名とパスワードで認証する。
	 * ユーザ詳細からJWT Cookie作成
	 * JWT Cookieをヘッダに設定し、ユーザ詳細データを返却する。
	 *
	 * @param loginReq
	 * @return
	 */
	@PostMapping("/auth/signin")
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
		// セッションに認証情報を設定
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateJwtRefreshCookie(refreshToken.getRefreshToken());
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new UserInfoResponse(userDetails.getId(),
						userDetails.getUsername(), roles));
	}

	@PostMapping("/auth/refreshtoken")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
		if (!StringUtils.isEmpty(refreshToken)) {
			if (!refreshTokenService.verifyRefreshTokenExp(refreshToken)) {
				return ResponseEntity.ok().body(new MessageResponse("Refresh Token has expired."));
			}
			RefreshToken token = refreshTokenService.getToken(refreshToken)
					.orElseThrow(() -> new ResourceNotFoundException("Token", "token value", refreshToken));
			ResponseCookie jwtCookie = jwtUtils
					.generateJwtCookie((UserDetailsImpl)userDetailsService
							.loadUserByUsername(token.getUser().getUsername()));
			return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
					.body(new MessageResponse("JWT refreshed."));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty."));
	}

	/**
	 * ユーザ名、メールが存在しなかったら
	 * ロールを設定しユーザを保存する。
	 *
	 * @param req
	 * @return
	 */
	@PostMapping("/auth/signup")
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
		return ResponseEntity.ok(new MessageResponse("ユーザが登録されました。"));
	}

	/**
	 * ログイン中のユーザ名を取得
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
	 * ログイン中のユーザデータを取得
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
	 * ユーザをログアウトする。
	 *
	 * @param authentication
	 * @return
	 */
	@PostMapping("/auth/signout/{id}")
	public ResponseEntity<?> signoutUser(@PathVariable String id) {
		refreshTokenService.deleteToken(Integer.parseInt(id));
		ResponseCookie cleanJwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie cleanJwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, cleanJwtRefreshCookie.toString())
				.body(new MessageResponse("ログアウトしました。"));
	}
}

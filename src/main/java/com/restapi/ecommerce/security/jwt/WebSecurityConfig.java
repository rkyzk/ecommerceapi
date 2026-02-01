package com.restapi.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.restapi.ecommerce.security.jwt.AuthEntryPointJwt;
import com.restapi.ecommerce.security.jwt.AuthTokenFilter;
import com.restapi.ecommerce.security.jwt.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	/** ユーザ情報を取得するサービス */
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	/** 例外を処理するクラス */
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	/** authenticationJWTトークンフィルター */
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/**
	 * authenticationProviderとして
	 * UserDetailsServiceとPasswordEncoderを設定し、
	 * DaoAuthenticationProviderを返す
	 * @return
	 *
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * authConfigに基づく AuthenticationManagerを取得し返す
	 * @return
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * BCryptPasswordエンコーダーを返す
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * SecurityFilterChainを設定し返却する
	 *
	 * @return
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
		     // Stateless: SecurityContextはリクエストをプロセスした後、削除される。
		    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .authorizeHttpRequests(auth ->
	            auth.requestMatchers("/api/public/**", "/api/auth/**").permitAll()
	        // .requestMatchers("/api/admin/**").permitAll() // during devlopment
		    // .requestMatchers("/h2-console/**").permitAll()
	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		            .anyRequest().authenticated()
		);
		http.authenticationProvider(authenticationProvider());
		// authenticationJwtTokenFilterをUsernamePasswordAuthenticationFilterの前に追加
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers(headers -> headers.frameOptions(
				frameOptions -> frameOptions.sameOrigin()));
		return http.build();
	}

	// access from these addresses will be excluded from the security filter chain.
	// 下記URLからのアクセスを許容する。
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web -> web.ignoring().requestMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**"));
	}
}

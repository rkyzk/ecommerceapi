package com.restapi.ecommerce.security.jwt;

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

import com.restapi.ecommerce.security.jwt.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	/** Class that handles exceptions  */
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	/** JWT authentication filter */
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/**
	 * set instances of UserDetailsService class and
	 * of PasswordEncoder to DaoAuthenticationProvider
	 * and return it.
	 * 
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
	 * return authenticationManager
	 * @return
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Return an instance of BCryptPassword
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Set securityFilterChain and return it
	 *
	 * @param http
	 * @return
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
		     // Stateless: SecurityContext will be deleted after each request is handled.
		    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .authorizeHttpRequests(auth ->
	            auth.requestMatchers("/api/public/**", "/api/auth/**").permitAll()
	        // .requestMatchers("/api/admin/**").permitAll() // during devlopment
		    // .requestMatchers("/h2-console/**").permitAll()
	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		            .anyRequest().authenticated()
		);
		http.authenticationProvider(authenticationProvider());
		// Add authenticationJwtTokenFilter before UsernamePasswordAuthenticationFilter
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers(headers -> headers.frameOptions(
				frameOptions -> frameOptions.sameOrigin()));
		return http.build();
	}

	/**
	 * Access from these addresses will be excluded from the security filter chain.
	 * 
	 * @return
	 */
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

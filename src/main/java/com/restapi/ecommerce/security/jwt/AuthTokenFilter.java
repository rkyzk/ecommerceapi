package com.restapi.ecommerce.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.restapi.ecommerce.security.jwt.service.RefreshTokenServiceImpl;
import com.restapi.ecommerce.security.jwt.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
 * JWTが有効のときUserDetailsのオブジェクトを取得し、
 * 権限の情報などをAuthenticationオブジェクトに設定。
 * AuthenticationオブジェクトをSecurityContextHolderに認証情報を設定
 *
 * return AuthTokenFilter
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
	@Autowired
	RefreshTokenServiceImpl refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return (request.getServletPath().startsWith("/api/public") ||
        		request.getServletPath().startsWith("/api/auth") ||
        		request.getMethod().equals("OPTIONS"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	if (!shouldNotFilter(request)) {
	        logger.debug("AuthTokenフィルタが適用されます。URI: {}", request.getRequestURI());
            String jwt = parseJwt(request);
            String username = "";
            System.out.println("JWT: " + jwt);
            try {
            	username = jwtUtils.getUsernameFromJwtToken(jwt);
            } catch (ExpiredJwtException e) {
            	request.setAttribute("error", "expiredJwt");
            	System.out.println("expiredJwt");
            	throw e;
            }
            if (jwt == null) {
            	logger.error("Jwt is empty");
            } else {
            	try {
					if (jwtUtils.validateJwtToken(jwt)) {
						setAuthentication(request, username);
					}
				} catch (Exception ex) {
					throw new ServletException();
				}
            }
    	}
    	filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, String username) {
	    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	    UsernamePasswordAuthenticationToken authentication =
	        new UsernamePasswordAuthenticationToken(userDetails,
	        null,
	        userDetails.getAuthorities());
	    logger.debug("JWTから取得したユーザ権限: {}", userDetails.getAuthorities());
	    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // session idなど
	    SecurityContextHolder.getContext().setAuthentication(authentication);
    }

	/** リクエストのクッキーからjwtを取得し返す */
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}


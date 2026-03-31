package com.restapi.ecommerce.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * If JWT is valid, get authentication object and 
 * set authorization data (roles) to security context
 *
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
	@Autowired
	RefreshTokenServiceImpl refreshTokenService;

	@Value("${authtokenfilter.msg001}")
    private String msg001;

	@Value("${authtokenfilter.msg002}")
    private String msg002;

	@Value("${authtokenfilter.msg003}")
    private String msg003;

	@Value("${authtokenfilter.msg004}")
    private String msg004;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * set paths where filter will be skipped.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return (request.getServletPath().startsWith("/api/public") ||
        		request.getServletPath().startsWith("/api/auth") ||
        		request.getMethod().equals("OPTIONS"));
    }

    /**
     * Validate JWT in the request cookie
     * Throw exceptions if invalid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug(msg001, request.getRequestURI());
        	// "AuthToken filter will be applied. URI: "
        String jwt = jwtUtils.getJwtFromCookies(request);
        logger.debug(msg002, jwt);
        String username = "";
        try {
        	username = jwtUtils.getUsernameFromJwtToken(jwt);
        } catch (ExpiredJwtException e) {
        	request.setAttribute("error", "expiredJwt");
        	throw e;
        }
        if (jwt == null) {
        	logger.error(msg003); // "JWT is empty."
        } else {
        	try {
				if (jwtUtils.validateJwtToken(jwt)) {
					setAuthentication(request, username);
				}
			} catch (Exception ex) {
				throw new ServletException();
			}
        }
    	filterChain.doFilter(request, response);
    }

    /**
     * Get user details object and
     * set it to security context holder
     *
     * @param request
     * @param username
     */
    private void setAuthentication(HttpServletRequest request, String username) {
	    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	    UsernamePasswordAuthenticationToken authentication =
	        new UsernamePasswordAuthenticationToken(userDetails,
	        null,
	        userDetails.getAuthorities());
	    logger.debug(msg004, userDetails.getAuthorities()); // "User roles : {}"
	    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
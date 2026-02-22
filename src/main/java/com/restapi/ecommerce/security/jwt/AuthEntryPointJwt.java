package com.restapi.ecommerce.security.jwt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 例外発生時の処理
 * @author reikoyazaki
 *
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Value("${frontend.URL}")
	private String frontEndUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("認証エラー: {}", authException.getCause(), authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", frontEndUrl);
        final Map<String, Object> body = new HashMap<>();

	    String attr = (String)request.getAttribute("error");   
	    if (attr != null && attr.equals("expiredJwt")) {
	    	response.sendError(420, "Jwt has expired.");
	    } else {
	    	body.put("message", authException.getMessage());
	    	body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	    }
        body.put("error", "Unauthorized");    
        body.put("path", request.getServletPath());
        OutputStream responseStream = response.getOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
        responseStream.flush();
    }
}

package com.restapi.ecommerce.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.restapi.ecommerce.security.jwt.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/** JWTのユティリティクラス */
@Component
public class JwtUtils {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtUtils.class);

	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;

	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${spring.app.jwtCookieName}")
	private String jwtCookie;

	@Value("${spring.app.jwtRefreshCookieName}")
	private String jwtRefreshCookie;

	public String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public String getJwtFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtCookie);
	}

	public String getJwtRefreshFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, jwtRefreshCookie);
	}

	private ResponseCookie generateCookie(String name, String value, String path) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.path(path)
				.maxAge(24 * 60 * 60)
				.httpOnly(true)
				.secure(false) // development
				.build();
		return cookie;
    }

	/**
	 * ユーザ名よりJWTを作成、クッキーに設定しクッキーを返す
	 */
	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		return generateCookie(jwtCookie, jwt, "/api");
	}

	public ResponseCookie generateJwtRefreshCookie(String refreshToken) {
		return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshtoken");
	}

	/**
	 * 空のトークンとパスからクッキーを作成し返す。
	 */
	public ResponseCookie getCleanCookie(String name, String path) {
		ResponseCookie cookie = ResponseCookie.from(name, null)
				.path(path)
				.build();
		return cookie;
	}

	public ResponseCookie getCleanJwtCookie() {
		return getCleanCookie(jwtCookie, "/api");
	}

	public ResponseCookie getCleanJwtRefreshCookie() {
		return getCleanCookie(jwtRefreshCookie, "/api/auth/refreshtoken");
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key())
				.compact();
	}

	/**
	 * JWTよりユーザ名を取得し返す
	 */
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	/**
	 * jwtSecretをもとにキーを生成
	 */
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	/**
	 * jwtトークンを検証
	 */
	public boolean validateJwtToken(String authToken) throws Exception {
		try {
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
			return true;
		} catch (MalformedJwtException e) {
	        logger.error("JWTトークンが不正: {}", e.getMessage());
	        throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("JWTトークンがサポートされていません。: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWTのclaimsの値が不正です。: {}", e.getMessage());
            throw e;
        }
	}
}

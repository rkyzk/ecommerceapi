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

import io.jsonwebtoken.ExpiredJwtException;
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

	/** リクエストのCookieよりJWTを取得し返却 */
	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	/**
	 * ユーザ名よりJWTを作成、クッキーに設定しクッキーを返す
	 */
	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
				.path("/api")
				.maxAge(24 * 60 * 60)
				.httpOnly(true) // XSSを防ぐ
				.secure(true) // CookieがHttpsのみで送られる
				.build();
		return cookie;
	}

	/**
	 * 空のトークンとパスからクッキーを作成し返す。
	 */
	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
				.path("/api")
				.build();
		return cookie;
	}

	/**
	 * ユーザ名よりJWTを生成して返す
	 */
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
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
			return true;
		} catch (MalformedJwtException e) {
	        logger.error("JWTトークンが不正: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWTトークンが期限切れです: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWTトークンがサポートされていません。: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWTのclaimsの値が不正です。: {}", e.getMessage());
        }
        return false;
	}
}

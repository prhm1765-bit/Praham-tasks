package com.CustomerRegi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtService {

	@Value("${jwt.secret}")
	private String SECRET_KEY; // move to env later

	/**
	 * Method for generating token
	 * @param email is Customer's email
	 * @param role is Customer's role-either admin or customer
	 * @return token as string
	 * */
	public String generateToken(String email, String role, String tenantId, int customerId) {
		return Jwts.builder()
			.setSubject(email)
			.claim("role", role)
			.claim("tenantId", tenantId)
			.claim("customerId", customerId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 12 hour
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * Method for extracting username from JWT token
	 * @param token is JWT token
	 * @return claim data as string
	 * */
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	/**
	 * Method for checking is JWT token valid for the username or not
	 * @param token is JWT token
	 * @return valid or not valid in boolean
	 * */
	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	/**
	 * Method for extracting role from JWT token
	 * @param token is JWT token
	 * @return claim data as string
	 * */
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}

	/**
	 * Method for checking is JWT token expired
	 * @param token is JWT token
	 * @return expired or not expired in boolean
	 * */
	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	/**
	 * Method for extracting all data(claims) from JWT token
 	 * @param token is JWT token
	 * @return Claims as JWT data
	 * */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * Method for getting SignIn key for encoding
	 * @return Key as Encoded form
	 * */
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractTenantId(String token) {
		return extractAllClaims(token).get("tenantId", String.class);
	}

}

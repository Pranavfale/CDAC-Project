package com.talentbridge.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "TalentBridgeSecretKeyForJWTAuthentication2026Secure";

	private final SecretKey key;

	public JwtService() {
		this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String generateToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).signWith(key).compact();
	}

	public String extractEmail(String token) {

		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

		return claims.getSubject();
	}

	public boolean validateToken(String token) {

		try {

			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}

}
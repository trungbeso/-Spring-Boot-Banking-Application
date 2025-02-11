package com.trungbeso.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	@Value("${app.jwt-expiration}")
	private long jwtExpirationDate;

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);

		return Jwts.builder()
			  .subject(username)
			  .issuedAt(currentDate)
			  .expiration(expirationDate)
			  .signWith(key())
			  .compact();
	}

	private Key key() {
		byte[] bytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(bytes);
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser()
			  .setSigningKey(key())
			  .build()
			  .parseClaimsJws(token)
			  .getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				  .setSigningKey(key())
				  .build()
				  .parse(token);
			return true;
		} catch (ExpiredJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}
}

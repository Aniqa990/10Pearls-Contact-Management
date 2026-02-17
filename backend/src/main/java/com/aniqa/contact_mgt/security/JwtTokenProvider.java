package com.aniqa.contact_mgt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public String generateToken(String userId) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            return Jwts.builder()
                    .setSubject(userId)  // Subject = who this token belongs to
                    .setIssuedAt(new Date())  // When the token was created
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // When it expires
                    .signWith(key, SignatureAlgorithm.HS512)  // Sign with a secret key
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token: ", e);
            throw new RuntimeException("Could not generate JWT token");
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error("Error extracting userId from JWT token: ", e);
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public boolean isTokenValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}

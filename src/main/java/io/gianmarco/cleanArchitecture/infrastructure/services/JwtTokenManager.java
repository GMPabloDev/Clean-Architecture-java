package io.gianmarco.cleanArchitecture.infrastructure.services;

import io.gianmarco.cleanArchitecture.application.services.TokenManager;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.InvalidTokenException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenManager implements TokenManager {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtTokenManager(
        @Value("${jwt.secret.key}") String secretKey,
        @Value("${jwt.refresh.key}") String refreshKey
    ) {
        this.accessKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshKey.getBytes());
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("email", user.getEmail())
            .claim("name", user.getName())
            .claim("roles", List.copyOf(user.getRoles()))
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(20, ChronoUnit.MINUTES)))
            .signWith(accessKey)
            .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", "refresh")
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
            .signWith(refreshKey)
            .compact();
    }

    @Override
    public UUID validateAccessToken(String token) {
        return UUID.fromString(parseClaims(token, accessKey).getSubject());
    }

    @Override
    public UUID validateRefreshToken(String token) {
        Claims claims = parseClaims(token, refreshKey);
        String type = claims.get("type", String.class);
        if (!"refresh".equals(type)) {
            throw new JwtException("Token is not a refresh token");
        }
        return UUID.fromString(claims.getSubject());
    }

    private Claims parseClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}

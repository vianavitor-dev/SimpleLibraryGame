package com.vianavitor.simplelibrarygame.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {
    @Value("${jwt.token.secret}")
    private String secret;

    public String generateToken(User user) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String role = user.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NullPointerException("user role cannot be null"))
                .toString();

        return JWT.create()
                .withIssuer("simple-library-game")
                .withSubject(user.getUsername())
                .withExpiresAt(this.getExpireAt())
                .withClaim("role", role)
                .withClaim("id", user.getId())
                .sign(algorithm);
    }

    public String validateAndGetSubject(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant getExpireAt() {
        return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.UTC);
    }
}

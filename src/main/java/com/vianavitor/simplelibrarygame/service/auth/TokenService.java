package com.vianavitor.simplelibrarygame.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.token.secret}")
    private String secret;

    public String generateToken(User user) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("simple-library-game")
                .withSubject(user.getUsername())
                .withExpiresAt(this.getExpireAt())
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

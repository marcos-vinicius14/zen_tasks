package com.marcos.dev.zentasks.zen_task_api.users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidTokenException;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.TokenGenerationException;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    @Value("${api.security.token.expiration_hours}")
    private long expirationHours;

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUsername())
                    .withClaim("userId", user.getId().toString())
                    .withClaim("role", user.getRole().name())
                    .withIssuedAt(new Date())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Erro ao gerar o token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException("Token inválido ou expirado");
        }
    }

    public UUID extractUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);

            return UUID.fromString(jwt.getClaim("userId").asString());

        } catch (JWTVerificationException | IllegalArgumentException exception) {
            throw new InvalidTokenException("Token inválido ou expirado");
        }
    }

    public String extractRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);

            return jwt.getClaim("role").asString();

        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException("Token inválido ou expirado");
        }
    }

    private Date genExpirationDate() {
        return Date.from(
                LocalDateTime.now()
                        .plusHours(expirationHours)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }
}
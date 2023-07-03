package com.epam.esm.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${security.jwt.token.secret-key}")
    protected String secretKey;

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token is invalid: {}", e.getMessage());

            return false;
        }
    }
}

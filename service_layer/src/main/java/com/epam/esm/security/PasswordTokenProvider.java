package com.epam.esm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class PasswordTokenProvider extends TokenProvider {

    @Value("${security.jwt.token.expire-length-in-sec.password-token}")
    private int tokenExpirationInSec;

    public String generateJwtToken(String email, String password) {

        return Jwts.builder().setSubject(email).addClaims(Map.of("password", password)).setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plusSeconds(tokenExpirationInSec).toEpochMilli()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256).compact();
    }

    public String getEmail(String token) {

        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getPassword(String token) {

        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().get("password").toString();
    }
}

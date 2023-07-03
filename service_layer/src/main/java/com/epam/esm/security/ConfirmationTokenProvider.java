package com.epam.esm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class ConfirmationTokenProvider extends TokenProvider {

    @Value("${security.jwt.token.expire-length-in-sec.confirmation-token}")
    public int tokenExpirationInSec;

    public String generateJwtToken(String email) {

        return Jwts.builder().setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plusSeconds(tokenExpirationInSec).toEpochMilli()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256).compact();
    }

    public String getEmail(String token) {

        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}

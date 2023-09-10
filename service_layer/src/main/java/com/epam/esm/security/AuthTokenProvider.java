package com.epam.esm.security;

import com.epam.esm.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class AuthTokenProvider extends TokenProvider {

    private final UserService userService;

    @Value("${security.jwt.token.expire-length-in-sec.access-token}")
    private int accessTokenExpirationInSec;
    @Value("${security.jwt.token.expire-length-in-sec.refresh-token}")
    private int refreshTokenExpirationInSec;

    private static final String ACCESS_TOKEN_COOKIE_NAME  = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Autowired
    public AuthTokenProvider(UserService userService) {

        this.userService = userService;
    }

    public String generateAccessToken(String email) {

        return generateJwtToken(email, accessTokenExpirationInSec);
    }

    public String generateRefreshToken(String email) {

        return generateJwtToken(email, refreshTokenExpirationInSec);
    }

    private String generateJwtToken(String email, int tokenExpirationInSec) {

        return Jwts.builder().setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plusSeconds(tokenExpirationInSec).toEpochMilli()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256).compact();
    }

    public String getUsername(String token) {

        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public LocalDateTime getExpiration(String token) {

        return LocalDateTime.ofInstant(Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().getExpiration().toInstant(), ZoneId.systemDefault());
    }

    public void setAccessTokenCookie(String token, HttpServletResponse response) {

        setTokenCookie(token, ACCESS_TOKEN_COOKIE_NAME, accessTokenExpirationInSec, response);
    }

    public void setRefreshTokenCookie(String token, HttpServletResponse response) {

        setTokenCookie(token, REFRESH_TOKEN_COOKIE_NAME, refreshTokenExpirationInSec, response);
    }

    public void removeAccessTokenCookie(HttpServletResponse response) {

        setTokenCookie("", ACCESS_TOKEN_COOKIE_NAME, 0, response);
    }

    public void removeRefreshTokenCookie(HttpServletResponse response) {

        setTokenCookie("", REFRESH_TOKEN_COOKIE_NAME, 0, response);
    }

    private void setTokenCookie(String token, String tokenName, int maxAgeInSec, HttpServletResponse response) {

        Cookie cookie = new Cookie(tokenName, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeInSec);
        response.addCookie(cookie);
    }

    public Optional<String> resolveAccessToken(HttpServletRequest request) {

        return resolveToken(ACCESS_TOKEN_COOKIE_NAME, request);
    }

    public Optional<String> resolveRefreshToken(HttpServletRequest request) {

        return resolveToken(REFRESH_TOKEN_COOKIE_NAME, request);
    }

    private Optional<String> resolveToken(String tokenName, HttpServletRequest request) {

        Optional<Cookie> cookie = Optional.ofNullable(WebUtils.getCookie(request, tokenName));

        return cookie.map(Cookie::getValue);
    }

    public Authentication getAuthentication(String token) {

        String username = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token).getBody().getSubject();
        UserDetails user = userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(username, "", user.getAuthorities());
    }
}

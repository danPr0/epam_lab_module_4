package com.epam.esm.security.filter;

import com.epam.esm.dto.InvalidatedTokenDTO;
import com.epam.esm.security.AuthTokenProvider;
import com.epam.esm.service.InvalidatedTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthTokenProvider       authTokenProvider;
    private final InvalidatedTokenService invTokenService;

    @Autowired
    public AuthTokenFilter(AuthTokenProvider authTokenProvider, InvalidatedTokenService invTokenService) {

        this.authTokenProvider = authTokenProvider;
        this.invTokenService   = invTokenService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> accessToken  = authTokenProvider.resolveAccessToken(request);
        Optional<String> refreshToken = authTokenProvider.resolveRefreshToken(request);

        if (accessToken.isPresent() && authTokenProvider.validateToken(accessToken.get())) {
            createAuthentication(accessToken.get());
        } else if (refreshToken.isPresent() && authTokenProvider.validateToken(refreshToken.get()) &&
                invTokenService.getInvToken(refreshToken.get()).isEmpty()) {

            refreshAccessToken(refreshToken.get(), response);
        }

        filterChain.doFilter(request, response);
    }

    private void refreshAccessToken(String refreshToken, HttpServletResponse response) {

        invTokenService.addInvToken(
                new InvalidatedTokenDTO(refreshToken, authTokenProvider.getExpiration(refreshToken)));

        String email = authTokenProvider.getUsername(refreshToken);
        String accessToken = authTokenProvider.generateAccessToken(email);

        authTokenProvider.setAccessTokenCookie(accessToken, response);
        authTokenProvider.setRefreshTokenCookie(authTokenProvider.generateRefreshToken(email), response);
        logger.info("Access token was refreshed for user with email " + email);

        createAuthentication(accessToken);
    }

    public void createAuthentication(String accessToken) {

        Authentication authentication = authTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

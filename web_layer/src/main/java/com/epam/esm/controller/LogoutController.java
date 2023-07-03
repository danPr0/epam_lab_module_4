package com.epam.esm.controller;

import com.epam.esm.security.AuthTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final AuthTokenProvider authTokenProvider;

    @Autowired
    public LogoutController(AuthTokenProvider authTokenProvider) {

        this.authTokenProvider = authTokenProvider;
    }

    @GetMapping
    public ResponseEntity<Object> logout(HttpServletResponse response) {

        authTokenProvider.removeAccessTokenCookie(response);
        authTokenProvider.removeRefreshTokenCookie(response);

        return ResponseEntity.ok().build();
    }
}

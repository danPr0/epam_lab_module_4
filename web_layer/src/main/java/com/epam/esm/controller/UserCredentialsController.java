package com.epam.esm.controller;

import com.epam.esm.rest.ChangePasswordRequest;
import com.epam.esm.security.PasswordTokenProvider;
import com.epam.esm.service.EmailService;
import com.epam.esm.service.UserService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
public class UserCredentialsController {

    private final UserService           userService;
    private final EmailService          emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordTokenProvider passwordTokenProvider;

    @Value("${http.host}")
    private String host;

    @Autowired
    public UserCredentialsController(
            UserService userService, EmailService emailService, AuthenticationManager authenticationManager,
            PasswordTokenProvider passwordTokenProvider) {

        this.userService           = userService;
        this.emailService          = emailService;
        this.authenticationManager = authenticationManager;
        this.passwordTokenProvider = passwordTokenProvider;
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentication.getName(), request.getOldPassword()));
            userService.changePassword(authentication.getName(), request.getNewPassword());
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Password is incorrect.");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(
            @Valid @RequestParam(required = false) Optional<String> email, Authentication authentication) {

        String userEmail;
        if (authentication != null) {
            userEmail = authentication.getName();
        } else if (email.isPresent() && userService.getUser(email.get()).isPresent()) {
            userEmail = email.get();
        } else {
            return ResponseEntity.badRequest().body("Email not found.");
        }

        String newPassword = RandomStringUtils.randomAlphanumeric(20);
        emailService.sendPasswordResetEmail(userEmail, newPassword,
                String.format("http://%s/confirm-password-reset?token=", host) +
                        passwordTokenProvider.generateJwtToken(userEmail, newPassword));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm-password-reset")
    public ResponseEntity<Object> confirmPasswordReset(@RequestParam String token) {

        if (!passwordTokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }

        userService.changePassword(passwordTokenProvider.getEmail(token), passwordTokenProvider.getPassword(token));

        return ResponseEntity.ok().build();
    }
}

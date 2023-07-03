package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.auth_request.SignInRequest;
import com.epam.esm.rest.auth_request.SignUpRequest;
import com.epam.esm.security.AuthTokenProvider;
import com.epam.esm.security.ConfirmationTokenProvider;
import com.epam.esm.service.EmailService;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.ProviderName;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService               userService;
    private final EmailService              emailService;
    private final AuthenticationManager     authenticationManager;
    private final AuthTokenProvider         authTokenProvider;
    private final ConfirmationTokenProvider confirmationTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${http.host}")
    private String host;

    @Autowired
    public AuthController(
            UserService userService, EmailService emailService, AuthenticationManager authenticationManager,
            AuthTokenProvider authTokenProvider, ConfirmationTokenProvider confirmationTokenProvider) {

        this.userService               = userService;
        this.emailService              = emailService;
        this.authenticationManager     = authenticationManager;
        this.authTokenProvider         = authTokenProvider;
        this.confirmationTokenProvider = confirmationTokenProvider;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn(@Valid @RequestBody SignInRequest request, HttpServletResponse response) {

        Optional<UserDTO> user = userService.getUser(request.getEmail());
        if (user.isPresent() && !user.get().getProvider().equals(ProviderName.LOCAL)) {
            return ResponseEntity.badRequest()
                    .body(String.format("You're signed up with %s provider. Please use the same provider to sign in.",
                            user.get().getProvider()));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword(),
                            userService.loadUserByUsername(request.getEmail()).getAuthorities()));
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.badRequest().body("Bad credentials.");
        }

        authTokenProvider.setAccessTokenCookie(authTokenProvider.generateAccessToken(request.getEmail()), response);
        authTokenProvider.setRefreshTokenCookie(authTokenProvider.generateRefreshToken(request.getEmail()), response);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequest req) {

        if (!userService.registerUser(
                UserDTO.builder().email(req.getEmail()).firstName(req.getFirstName()).lastName(req.getLastName())
                        .build(), req.getPassword(), ProviderName.LOCAL)) {

            return ResponseEntity.badRequest().body(String.format("User already exists (email = %s).", req.getEmail()));
        } else {
            String confirmUrl = String.format("http://%s/auth/confirm-signup?token=", host) +
                    confirmationTokenProvider.generateJwtToken(req.getEmail());
            emailService.sendSignupConfirmationEmail(req.getEmail(), confirmUrl);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm-signup")
    public ResponseEntity<Object> confirmSignUp(@RequestParam String token, HttpServletResponse response) {

        if (!confirmationTokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest().body("Token is expired.");
        }

        String email = confirmationTokenProvider.getEmail(token);
        if (!userService.enableUser(email)) {
            return ResponseEntity.badRequest().body("User was already authorized.");
        }
        authTokenProvider.setAccessTokenCookie(authTokenProvider.generateAccessToken(email), response);
        authTokenProvider.setRefreshTokenCookie(authTokenProvider.generateRefreshToken(email), response);

        return ResponseEntity.ok().build();
    }
}

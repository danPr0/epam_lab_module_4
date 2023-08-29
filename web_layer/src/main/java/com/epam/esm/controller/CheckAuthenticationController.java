package com.epam.esm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/check-authentication")
public class CheckAuthenticationController {

    @GetMapping
    public ResponseEntity<Object> checkAuthentication() {
        return ResponseEntity.ok().build();
    }
}

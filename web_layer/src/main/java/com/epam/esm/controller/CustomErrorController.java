package com.epam.esm.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> processErrorInResponse(HttpServletRequest request) {

        int errorStatus = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return ResponseEntity.status(errorStatus)
                .body(Map.of("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE), "errorCode",
                        errorStatus));
    }
}

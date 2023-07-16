package com.epam.esm.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Object> processErrorInResponse(HttpServletRequest request) {

        int errorStatus = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return ResponseEntity.status(errorStatus)
                .body(Map.of("errorMessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE), "errorCode",
                        errorStatus));
    }
}

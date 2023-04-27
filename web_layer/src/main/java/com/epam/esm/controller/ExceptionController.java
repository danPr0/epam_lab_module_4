package com.epam.esm.controller;


import jakarta.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller class responsible for globally handling server exceptions.
 *
 * @author Danylo Proshyn
 */

@ControllerAdvice
public class ExceptionController {

    private final Logger logger = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<?> invalidRequest(ValidationException ex) {

        logger.error(ex);

        return ResponseEntity.badRequest().body("Cannot process the request.");
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleGeneralException(Exception ex) {

        logger.error(ex);

        return ResponseEntity.badRequest().body("Something went wrong. Please try again.");
    }
}

package com.epam.esm.controller.resource_controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controller class responsible for operations with users.
 *
 * @author Danylo Proshyn
 */

@Controller
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    private static final String RESOURCE_CODE = "03";

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<Object> getUser(Authentication authentication) {

        String email = authentication.getName();
        Optional<UserDTO> user = userService.getUser(email);

        if (user.isPresent()) {
            user.get().add(linkTo(methodOn(UserController.class).getUser(authentication)).withSelfRel());

            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not found (email = %s)", email),
                            "errorCode", "404" + RESOURCE_CODE));
        }
    }
}

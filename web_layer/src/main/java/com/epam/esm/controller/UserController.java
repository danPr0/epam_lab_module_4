package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final String resourceCode = "03";

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {

        Optional<UserDTO> user = userService.getUser(id);

        if (user.isPresent()) {
            user.get().add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());

            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not found (id = %s)", id),
                            "errorCode", "404" + resourceCode));
        }
    }
}

package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.rest.AddOrderRequest;
import com.epam.esm.service.OrderService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controller class responsible for operations with orders.
 *
 * @author Danylo Proshyn
 */

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final String resourceCode = "04";
    private final Logger logger       = LogManager.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<OrderDTO>> getUserOrders(@RequestParam Long userId) {

        return ResponseEntity.ok(CollectionModel.of(orderService.getUserOrders(userId),
                linkTo(methodOn(OrderController.class).getUserOrders(userId)).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(@Valid @RequestBody AddOrderRequest req) {

        if (orderService.addOrder(req.getUserId(), req.getGiftCertificateId())) {
            return ResponseEntity.status(201).body(new RepresentationModel<>().add(
                    linkTo(methodOn(OrderController.class).getUserOrders(req.getUserId())).withRel("user_orders")));
        } else {
            String errorMsg =
                    String.format("User resource with id = %s or GiftCertificate resource with id = %s doesn't exist",
                            req.getUserId(), req.getGiftCertificateId());
            logger.error(errorMsg);

            return ResponseEntity.status(409).body(Map.of("errorMessage", errorMsg, "errorCode", "409" + resourceCode));
        }
    }
}

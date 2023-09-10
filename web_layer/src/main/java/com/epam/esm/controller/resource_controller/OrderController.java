package com.epam.esm.controller.resource_controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.resource_request.AddOrderRequest;
import com.epam.esm.service.OrderService;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private static final String RESOURCE_CODE = "04";
    private final Logger        logger        = LogManager.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<OrderDTO>> getUserOrders(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer total,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(CollectionModel.of(orderService.getUserOrders(email, page, total),
                linkTo(methodOn(OrderController.class).getUserOrders(page, total, authentication)).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(@RequestBody AddOrderRequest request, Authentication authentication) {

        String  email = authentication.getName();
        UserDTO user  = UserDTO.builder().email(email).build();
        List<GiftCertificateDTO> gcList =
                request.getGcIdList().stream().map(gcId -> GiftCertificateDTO.builder().id(gcId).build()).toList();

        if (orderService.addOrder(OrderDTO.builder().user(user).gcList(gcList).build())) {
            return ResponseEntity.status(201).body(new RepresentationModel<>().add(
                    linkTo(methodOn(OrderController.class).getUserOrders(1, Integer.MAX_VALUE, authentication)).withRel(
                            "user_orders")));
        } else {
            String errorMsg = "Resource gift certificate doesn't exist";
            logger.error(errorMsg);

            return ResponseEntity.status(404).body(Map.of("errorMessage", errorMsg, "errorCode", "404" + RESOURCE_CODE));
        }
    }
}

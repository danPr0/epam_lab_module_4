package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Service class for {@link OrderDTO}.
 *
 * @author Danylo Proshyn
 */

public interface OrderService {

    List<OrderDTO> getUserOrders(String email, int page, int total);

    boolean addOrder(@Valid OrderDTO orderDTO);
}

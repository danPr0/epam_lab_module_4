package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;

import java.util.List;

/**
 * Service class for {@link OrderDTO}.
 *
 * @author Danylo Proshyn
 */

public interface OrderService {

    List<OrderDTO> getUserOrders(Long userId);

    boolean addOrder(Long userId, Long gcId);
}

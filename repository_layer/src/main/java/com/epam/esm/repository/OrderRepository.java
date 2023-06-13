package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO class for {@link Order} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
public interface OrderRepository {

    Order insertEntity(Order order);

    List<Order> getEntitiesByUser(Long userId);
}

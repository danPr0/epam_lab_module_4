package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO class for {@link Order} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserEmail(String email, Pageable pageable);
}

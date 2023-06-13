package com.epam.esm.service_impl;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.DTOUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of service interface {@link UserService}.
 *
 * @author Danylo Proshyn
 */

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository           orderRepository;
    private final UserRepository            userRepository;
    private final GiftCertificateRepository gcRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository, UserRepository userRepository, GiftCertificateRepository gcRepository) {

        this.orderRepository = orderRepository;
        this.userRepository  = userRepository;
        this.gcRepository    = gcRepository;
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {

        return orderRepository.getEntitiesByUser(userId).stream().map(DTOUtil::convertToDTO).toList();
    }

    @Override
    public boolean addOrder(Long userId, Long gcId) {

        Optional<User>            user = userRepository.getEntity(userId);
        Optional<GiftCertificate> gc   = gcRepository.getEntity(gcId);

        if (user.isPresent() && gc.isPresent() && gc.get().isActive()) {
            orderRepository.insertEntity(
                    new Order(user.get(), gc.get(), gc.get().getPrice()));

            gc.get().setActive(false);
            gcRepository.updateEntity(gc.get());
        } else {
            return false;
        }

        return true;
    }
}

package com.epam.esm.service_impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Order_;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.DTOUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of service interface {@link UserService}.
 *
 * @author Danylo Proshyn
 */

@Service
@Validated
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
    public List<OrderDTO> getUserOrders(String email, int page, int total) {

        return orderRepository.findAllByUserEmail(email,
                                      PageRequest.of(page - 1, total,
                                              Sort.by(Sort.Direction.ASC, Order_.CREATED_DATE))).getContent().stream()
                              .map(DTOUtil::convertToDTO).toList();
    }

    @Override
    public boolean addOrder(@Valid OrderDTO orderDTO) {

        try {
            Optional<User> user = userRepository.findByEmail(orderDTO.getUser().getEmail());
            if (user.isEmpty()) {
                throw new EntityNotFoundException();
            }

            Order order = new Order();
            order.setUser(user.get());

            List<GiftCertificate> gcList = new ArrayList<>();
            for (GiftCertificateDTO gcDTO : orderDTO.getGcList()) {
                Optional<GiftCertificate> gc = gcRepository.findById(gcDTO.getId());
                if (gc.isEmpty() ||
                        gc.get().getCreatedDate().plusDays(gc.get().getDuration()).isBefore(LocalDateTime.now())) {
                    throw new EntityNotFoundException();
                }
                gcList.add(gc.get());
            }
            order.setGiftCertificates(gcList);
            order.setCost(gcList.stream().mapToDouble(GiftCertificate::getPrice).sum());

            orderRepository.save(order);
        } catch (EntityNotFoundException e) {
            return false;
        }

        return true;
    }
}

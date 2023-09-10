package service_test;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service_impl.OrderServiceImpl;
import com.epam.esm.util_service.DTOUtil;
import com.epam.esm.util_service.ProviderName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {OrderServiceImpl.class})
class OrderServiceTest extends Mockito {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository           orderRepository;
    @MockBean
    private UserRepository            userRepository;
    @MockBean
    private GiftCertificateRepository gcRepository;

    private final User user;

    private final GiftCertificate gc1;
    private final GiftCertificate gc2;

    private final Order order;

    private final OrderDTO orderDTO;

    {
        user = User.builder().id(1L).email("1").password("1").username("1").firstName("1").lastName("1")
                .provider(Provider.builder().name(ProviderName.LOCAL.name()).build()).build();

        LocalDateTime createdDate      = LocalDateTime.now();
        LocalDateTime lastModifiedDate = LocalDateTime.now();

        gc1 = new GiftCertificate(1L, "1", "1", 1, 1, List.of());
        gc1.setCreatedDate(createdDate);
        gc1.setLastModifiedDate(lastModifiedDate);

        gc2 = new GiftCertificate(2L, "2", "2", 2, 2, List.of());
        gc2.setCreatedDate(createdDate);
        gc2.setLastModifiedDate(lastModifiedDate);

        LocalDateTime purchaseTimestamp = LocalDateTime.now();

        order = new Order(1L, user, List.of(gc1, gc2), gc1.getPrice() + gc2.getPrice());
        order.setCreatedDate(purchaseTimestamp);

        orderDTO = new OrderDTO(1L, order.getCost(), order.getCreatedDate(), DTOUtil.convertToDTO(user),
                List.of(DTOUtil.convertToDTO(gc1), DTOUtil.convertToDTO(gc2)));
    }

    @Test
    void testGetUserOrders() {

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, Order_.CREATED_DATE));
        when(orderRepository.findAllByUserEmail(user.getEmail(), pageRequest)).thenReturn(new PageImpl<>(List.of(order)));

        assertEquals(List.of(orderDTO), orderService.getUserOrders(user.getEmail(), 1, 1));
        verify(orderRepository).findAllByUserEmail(user.getEmail(), pageRequest);
    }

    @Test
    void testAddOrderSuccess() {

        Order orderToAdd = order.clone();
        orderToAdd.setId(null);
        orderToAdd.setCreatedDate(null);
        orderToAdd.setLastModifiedDate(null);

        OrderDTO orderDTOToAdd = orderDTO.clone();
        orderDTOToAdd.setId(null);
        orderDTOToAdd.setCost(null);
        orderDTOToAdd.setTimestamp(null);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(gcRepository.findById(gc1.getId())).thenReturn(Optional.of(gc1));
        when(gcRepository.findById(gc2.getId())).thenReturn(Optional.of(gc2));
        when(orderRepository.save(orderToAdd)).thenReturn(order);

        assertTrue(orderService.addOrder(orderDTOToAdd));
        verify(orderRepository).save(orderToAdd);
    }

    @Test
    void testAddOrderFailOnEmptyUser() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertFalse(orderService.addOrder(orderDTO));
    }

    @Test
    void testAddOrderFailOnEmptyGiftCertificate() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(gcRepository.findById(gc1.getId())).thenReturn(Optional.empty());

        assertFalse(orderService.addOrder(orderDTO));
    }
}

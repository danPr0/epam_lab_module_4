package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class for {@link Order} entity.
 *
 * @author Danylo Proshyn
 */

@Relation(collectionRelation = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class OrderDTO extends RepresentationModel<TagDTO> implements Cloneable {

    @Positive
    private Long id;

    @Positive
    @Digits(integer = 10, fraction = 3)
    private Double        cost;
    private LocalDateTime timestamp;

    @NotNull
    private UserDTO user;

    @NotNull
    private List<GiftCertificateDTO> gcList;

    @Override
    public OrderDTO clone() {

        try {
            return (OrderDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

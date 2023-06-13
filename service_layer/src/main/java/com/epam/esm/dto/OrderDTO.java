package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.time.LocalDateTime;

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
public class OrderDTO extends RepresentationModel<TagDTO> {

    @NotNull
    private Long userId;

    @NotNull
    private Long giftCertificateId;

    @Positive
    @Digits(integer = 10, fraction = 3)
    private Double        cost;
    private LocalDateTime timestamp;
}

package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.sql.Timestamp;

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

    private Long userId;
    private Long giftCertificateId;
    private Double cost;
    private Timestamp timestamp;
}

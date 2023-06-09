package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * DTO class for {@link GiftCertificate} entity.
 *
 * @author Danylo Proshyn
 */

@Relation(collectionRelation = "giftCertificates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {

    private long    id;
    private String  name;
    private String  description;
    private Double  price;
    private Integer duration;
    private String  createDate;
    private String  lastUpdateDate;

    private List<TagDTO> tags;
}

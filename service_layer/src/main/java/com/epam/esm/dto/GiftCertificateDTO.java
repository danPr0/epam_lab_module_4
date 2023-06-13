package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
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

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 3)
    private Double price;

    @NotNull
    @Positive
    private Integer       duration;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    @NotNull
    private List<TagDTO> tags;
}

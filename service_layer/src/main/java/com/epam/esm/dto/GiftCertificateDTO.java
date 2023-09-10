package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import jakarta.validation.constraints.*;
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
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> implements Cloneable {

    @Positive
    private Long id;

    @NotBlank
    @Size(min = 6, max = 30)
    private String name;

    @NotBlank
    @Size(min = 12, max = 1000)
    private String description;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @NotNull
    @PositiveOrZero
    private Integer       duration;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    @NotNull
    private List<TagDTO> tags;

    @Override
    public GiftCertificateDTO clone() {

        try {
            return (GiftCertificateDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

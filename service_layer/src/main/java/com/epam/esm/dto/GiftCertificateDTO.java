package com.epam.esm.dto;

import lombok.*;

import java.util.List;

/**
 * DTO class for {@link com.epam.esm.entity.GiftCertificate} entity.
 *
 * @author Danylo Proshyn
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GiftCertificateDTO {

    private long    id;
    private String  name;
    private String  description;
    private Double  price;
    private Integer duration;
    private String  createDate;
    private String  lastUpdateDate;

    private List<TagDTO> tags;
}

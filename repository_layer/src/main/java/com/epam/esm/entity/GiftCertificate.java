package com.epam.esm.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity class for "gift_certificates" table.
 *
 * @author Danylo Proshyn
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GiftCertificate {

    private Long          id;
    private String        name;
    private String        description;
    private double        price;
    private int           duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}

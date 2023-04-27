package com.epam.esm.entity;

import lombok.*;

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
public class Tag {

    private Long   id;
    private String name;
}

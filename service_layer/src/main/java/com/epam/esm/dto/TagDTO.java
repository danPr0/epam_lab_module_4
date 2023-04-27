package com.epam.esm.dto;

import lombok.*;

/**
 * DTO class for {@link com.epam.esm.entity.Tag} entity.
 *
 * @author Danylo Proshyn
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class TagDTO {

    private long   id;
    private String name;
}

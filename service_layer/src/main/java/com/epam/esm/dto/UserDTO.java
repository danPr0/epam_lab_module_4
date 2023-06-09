package com.epam.esm.dto;

import com.epam.esm.entity.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO class for {@link User} entity.
 *
 * @author Danylo Proshyn
 */

@Relation(collectionRelation = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDTO extends RepresentationModel<TagDTO> {

    private Long id;
    private String email;
}

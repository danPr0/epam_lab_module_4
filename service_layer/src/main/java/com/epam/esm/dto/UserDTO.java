package com.epam.esm.dto;

import com.epam.esm.entity.User;
import com.epam.esm.util_service.ProviderName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    @Email
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private ProviderName provider;
}

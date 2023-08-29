package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO class for {@link Tag} entity.
 *
 * @author Danylo Proshyn
 */

@Relation(collectionRelation = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class TagDTO extends RepresentationModel<TagDTO> {

    @NotBlank
    @Size(min = 3, max = 15)
    private String name;
}

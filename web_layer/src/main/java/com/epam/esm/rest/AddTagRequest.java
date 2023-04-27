package com.epam.esm.rest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Class is used as request body when adding tag.
 *
 * @author Danylo Proshyn
 */

@Getter
@Setter
public class AddTagRequest {

    @NotNull
    private Long   id;
    @NotNull
    private String name;
}

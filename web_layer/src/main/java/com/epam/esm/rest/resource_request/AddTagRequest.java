package com.epam.esm.rest.resource_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, max = 15)
    private String name;
}

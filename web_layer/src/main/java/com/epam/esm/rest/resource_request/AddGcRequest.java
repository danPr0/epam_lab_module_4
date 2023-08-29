package com.epam.esm.rest.resource_request;


import com.epam.esm.dto.TagDTO;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Class is used as request body when adding gift certificate.
 *
 * @author Danylo Proshyn
 */

@Getter
@Setter
public class AddGcRequest {

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
    private Integer duration;

    @NotNull
    private List<TagDTO> tags;
}

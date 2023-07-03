package com.epam.esm.rest.resource_request;


import com.epam.esm.dto.TagDTO;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    private List<TagDTO> tags;
}

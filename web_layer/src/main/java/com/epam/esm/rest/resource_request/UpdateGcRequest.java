package com.epam.esm.rest.resource_request;

import com.epam.esm.dto.TagDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class is used as request body when updating gift certificate.
 *
 * @author Danylo Proshyn
 */

@Getter
@Setter
public class UpdateGcRequest {

    private String name;
    private String description;

    @Positive
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @Positive
    private Integer duration;

    private List<TagDTO> tags;
}

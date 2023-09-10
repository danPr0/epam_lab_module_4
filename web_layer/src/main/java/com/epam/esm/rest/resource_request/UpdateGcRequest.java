package com.epam.esm.rest.resource_request;

import com.epam.esm.dto.TagDTO;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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

    @Size(min = 6, max = 30)
    private String name;

    @Size(min = 12, max = 1000)
    private String description;

    @Positive
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @PositiveOrZero
    private Integer duration;

    private List<TagDTO> tags;
}

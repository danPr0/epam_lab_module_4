package com.epam.esm.rest.resource_request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class is used as request body when creating user request.
 *
 * @author Danylo Proshyn
 */

@Getter
@Setter
public class AddOrderRequest {

    @NotNull
    private List<Long> gcIdList;
}

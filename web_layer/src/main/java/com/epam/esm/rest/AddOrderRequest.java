package com.epam.esm.rest;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Class is used as request body when creating user request.
 *
 * @author Danylo Proshyn
 */

@Getter
@Setter
public class AddOrderRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long giftCertificateId;
}

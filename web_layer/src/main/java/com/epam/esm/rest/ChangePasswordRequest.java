package com.epam.esm.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    @Size(min = 8)
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}

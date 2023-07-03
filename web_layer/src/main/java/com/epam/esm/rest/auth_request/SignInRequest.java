package com.epam.esm.rest.auth_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {

    @NotNull
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}

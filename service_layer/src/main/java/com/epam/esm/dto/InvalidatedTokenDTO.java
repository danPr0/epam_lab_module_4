package com.epam.esm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvalidatedTokenDTO {

    @NotBlank
    private String token;

    @NotNull
    private LocalDateTime expiryDate;
}

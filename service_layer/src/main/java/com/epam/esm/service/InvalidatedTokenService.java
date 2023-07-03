package com.epam.esm.service;

import com.epam.esm.dto.InvalidatedTokenDTO;
import jakarta.validation.Valid;

import java.util.Optional;

public interface InvalidatedTokenService {

    Optional<InvalidatedTokenDTO> getInvToken(String token);

    void addInvToken(@Valid InvalidatedTokenDTO invToken);
}

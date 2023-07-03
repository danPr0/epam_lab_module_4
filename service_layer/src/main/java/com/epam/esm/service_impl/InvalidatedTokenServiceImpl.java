package com.epam.esm.service_impl;

import com.epam.esm.dto.InvalidatedTokenDTO;
import com.epam.esm.repository.InvalidatedTokenRepository;
import com.epam.esm.service.InvalidatedTokenService;
import com.epam.esm.util_service.DTOUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    private final InvalidatedTokenRepository invTokenRepository;

    @Autowired
    public InvalidatedTokenServiceImpl(InvalidatedTokenRepository invTokenRepository) {

        this.invTokenRepository = invTokenRepository;
    }

    @Override
    public Optional<InvalidatedTokenDTO> getInvToken(String token) {

        return invTokenRepository.findByToken(token).map(DTOUtil::convertToDTO);
    }

    @Override
    public void addInvToken(@Valid InvalidatedTokenDTO invToken) {

        invTokenRepository.save(DTOUtil.convertToEntity(invToken));
    }
}

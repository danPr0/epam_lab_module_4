package com.epam.esm.service_impl;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.DTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of service interface {@link UserService}.
 *
 * @author Danylo Proshyn
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDTO> getUser(Long userId) {

        return userRepository.getEntity(userId).map(DTOUtil::convertToDTO);
    }
}

package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;

import java.util.Optional;

/**
 * Service class for {@link UserDTO}.
 *
 * @author Danylo Proshyn
 */


public interface UserService {

    Optional<UserDTO> getUser(Long userId);
}

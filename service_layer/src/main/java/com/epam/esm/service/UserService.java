package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.util_service.ProviderName;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Service class for {@link UserDTO}.
 *
 * @author Danylo Proshyn
 */


public interface UserService extends UserDetailsService {

    Optional<UserDTO> getUser(String email);

    boolean registerUser(@Valid UserDTO user, String password, ProviderName provider);

    boolean updateUser(@Valid UserDTO user);

    boolean enableUser(String email);

    boolean changePassword(String email, String newPassword);
}

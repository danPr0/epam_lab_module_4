package com.epam.esm.service_impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.repository.ProviderRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.ProviderName;
import com.epam.esm.util_service.RoleName;
import com.epam.esm.util_service.DTOUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Implementation of service interface {@link UserService}.
 *
 * @author Danylo Proshyn
 */

@Service
@Validated
@XRayEnabled
public class UserServiceImpl implements UserService {

    private final UserRepository     userRepository;
    private final RoleRepository     roleRepository;
    private final ProviderRepository providerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository, RoleRepository roleRepository,
            ProviderRepository providerRepository, PasswordEncoder passwordEncoder) {

        this.userRepository     = userRepository;
        this.roleRepository     = roleRepository;
        this.providerRepository = providerRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    @Override
    public Optional<UserDTO> getUser(String email) {

        return userRepository.findByEmail(email).map(DTOUtil::convertToDTO);
    }

    @Override
    public boolean registerUser(@Valid UserDTO userDTO, String password, ProviderName providerName) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return false;
        }

        User user = DTOUtil.convertToEntity(userDTO);

        if (providerName.equals(ProviderName.LOCAL)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setRole(roleRepository.findByName(RoleName.ROLE_USER.name()));
        user.setProvider(providerRepository.findByName(providerName.name()));
        user.setEnabled(!providerName.equals(ProviderName.LOCAL));
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean updateUser(@Valid UserDTO userDTO) {

        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isEmpty()) {
            return false;
        }

        user.get().setFirstName(userDTO.getFirstName());
        user.get().setLastName(userDTO.getLastName());
        user.get().setUsername(userDTO.getUsername());
        userRepository.save(user.get());

        return true;
    }

    @Override
    public boolean enableUser(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || user.get().isEnabled()) {
            return false;
        }
        user.get().setEnabled(true);
        userRepository.save(user.get());

        return true;
    }

    @Override
    public boolean changePassword(String email, String newPassword) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User not found (username = %s).", username)));
    }
}

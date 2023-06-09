package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * DAO class for {@link User} entity.
 *
 * @author Danylo Proshyn
 */

public interface UserRepository {

    Optional<User> getEntity(Long id);
}

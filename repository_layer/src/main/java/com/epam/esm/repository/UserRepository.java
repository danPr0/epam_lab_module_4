package com.epam.esm.repository;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO class for {@link User} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
@XRayEnabled
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}

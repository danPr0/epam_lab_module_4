package com.epam.esm.repository_impl;


import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of DAO Interface {@link UserRepository}.
 *
 * @author Danylo Proshyn
 */


@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<User> getEntity(Long id) {

        return Optional.ofNullable(em.find(User.class, id));
    }
}

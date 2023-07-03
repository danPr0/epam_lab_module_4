package com.epam.esm.repository;

import com.epam.esm.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {

    Optional<InvalidatedToken> findByToken(String token);
}

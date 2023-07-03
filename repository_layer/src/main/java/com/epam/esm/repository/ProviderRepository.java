package com.epam.esm.repository;

import com.epam.esm.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Provider findByName(String name);
}

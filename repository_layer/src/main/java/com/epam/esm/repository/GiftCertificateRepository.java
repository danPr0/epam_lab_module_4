package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO class for {@link GiftCertificate} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, GiftCertificateRepositoryCustom {

}

package com.epam.esm.repository;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO class for {@link GiftCertificate} entity.
 *
 * @author Danylo Proshyn
 */

@Repository
@XRayEnabled
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, GiftCertificateRepositoryCustom {

}

package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * DAO class for {@link com.epam.esm.entity.Tag} entity.
 *
 * @author Danylo Proshyn
 */

public interface TagRepository {

    void insertEntity(Tag tag);

    Optional<Tag> getEntity(Long id);

    int deleteEntity(Long id);

    List<Tag> getAllByGiftCertificate(Long giftCertificateId);
}

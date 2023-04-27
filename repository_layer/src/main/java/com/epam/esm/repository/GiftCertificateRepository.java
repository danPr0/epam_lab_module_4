package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * DAO class for {@link com.epam.esm.entity.GiftCertificate} entity.
 *
 * @author Danylo Proshyn
 */

public interface GiftCertificateRepository {

    void insertEntity(GiftCertificate gc);

    Optional<GiftCertificate> getEntity(Long id);

    int updateEntity(GiftCertificate gc);

    int deleteEntity(Long id);

    /**
     * Method gets all gift certificates with optional filter by tag name, part of name or part of description
     * and optional sort by name or creation date.
     */
    List<GiftCertificate> getAll(
            Optional<String> tagName, Optional<String> namePart, Optional<String> descriptionPart,
            Optional<String> nameOrder, Optional<String> createDateOrder);

    void addTagToEntity(Long gcId, Long tagId);

    int deleteAllTagsForEntity(Long gcId);
}

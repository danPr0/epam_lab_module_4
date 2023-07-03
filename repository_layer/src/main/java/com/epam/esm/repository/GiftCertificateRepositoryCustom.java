package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepositoryCustom {

    /**
     * Method gets all gift certificates with optional filter by tag name, part of name or part of description
     * and optional sort by name or creation date.
     */
    List<GiftCertificate> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<String> nameOrder, Optional<String> createDateOrder);
}

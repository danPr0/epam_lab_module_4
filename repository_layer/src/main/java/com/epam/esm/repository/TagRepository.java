package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * DAO class for {@link Tag} entity.
 *
 * @author Danylo Proshyn
 */

public interface TagRepository {

    void insertEntity(Tag tag);

    Optional<Tag> getEntity(Long id);

    Optional<Tag> getMostPopularEntity(Long userId);

    void deleteEntity(Long id);
}

package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagRepositoryCustom {

    Optional<Tag> complexJPQLQuery();
}

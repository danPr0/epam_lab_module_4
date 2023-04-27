package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.Optional;

/**
 * Service class for {@link com.epam.esm.dto.TagDTO}.
 *
 * @author Danylo Proshyn
 */

public interface TagService {

    boolean addTag(TagDTO tag);

    Optional<TagDTO> getTag(long id);

    boolean deleteTag(long id);
}

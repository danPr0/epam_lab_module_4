package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import jakarta.validation.Valid;

import java.util.Optional;

/**
 * Service class for {@link TagDTO}.
 *
 * @author Danylo Proshyn
 */

public interface TagService {

    boolean addTag(@Valid TagDTO tag);

    Optional<TagDTO> getTag(String name);

    Optional<TagDTO> getMostPopularUserTag();

    boolean deleteTag(String name);
}

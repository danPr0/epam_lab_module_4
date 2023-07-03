package com.epam.esm.service_impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util_service.DTOUtil;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Implementation of service interface {@link TagService}.
 *
 * @author Danylo Proshyn
 */

@Service
@Validated
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {

        this.tagRepository = tagRepository;
    }

    @Override
    public boolean addTag(@Valid TagDTO tag) {

        if (tagRepository.findByName(tag.getName()).isPresent()) {
            return false;
        }
        tagRepository.save(DTOUtil.convertToEntity(tag));

        return true;
    }

    @Override
    public Optional<TagDTO> getTag(String name) {

        return tagRepository.findByName(name).map(DTOUtil::convertToDTO);
    }

    @Override
    public Optional<TagDTO> getMostPopularUserTag() {

        return tagRepository.complexJPQLQuery().map(DTOUtil::convertToDTO);
    }

    @Override
    public boolean deleteTag(String name) {

        if (tagRepository.findByName(name).isEmpty()) {
            return false;
        }
        tagRepository.deleteByName(name);

        return true;
    }
}

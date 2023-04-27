package com.epam.esm.util_service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * Class for converting DTOs to entities and vice versa.
 *
 * @author Danylo Proshyn
 */

public class DTOUtil {

    public static GiftCertificateDTO convertToDTO(GiftCertificate gc, List<Tag> tags) {

        return GiftCertificateDTO.builder().id(gc.getId()).name(gc.getName()).description(gc.getDescription())
                .price(gc.getPrice()).duration(gc.getDuration()).createDate(gc.getCreateDate().toString())
                .lastUpdateDate(gc.getLastUpdateDate().toString())
                .tags(tags.stream().map(DTOUtil::convertToDTO).toList()).build();
    }

    public static TagDTO convertToDTO(Tag tag) {

        return TagDTO.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public static GiftCertificate convertToEntity(GiftCertificateDTO gcDTO) {

        return GiftCertificate.builder().id(gcDTO.getId()).name(gcDTO.getName()).description(gcDTO.getDescription())
                .price(gcDTO.getPrice()).duration(gcDTO.getDuration()).build();
    }

    public static Tag convertToEntity(TagDTO tagDTO) {

        return Tag.builder().id(tagDTO.getId()).name(tagDTO.getName()).build();
    }
}

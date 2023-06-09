package com.epam.esm.util_service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;

/**
 * Class for converting DTOs to entities and vice versa.
 *
 * @author Danylo Proshyn
 */

public class DTOUtil {

    public static GiftCertificateDTO convertToDTO(GiftCertificate gc) {

        return GiftCertificateDTO.builder().id(gc.getId()).name(gc.getName()).description(gc.getDescription())
                .price(gc.getPrice()).duration(gc.getDuration()).createDate(gc.getCreateDate().toString())
                .lastUpdateDate(gc.getLastUpdateDate().toString())
                .tags(gc.getTags().stream().map(DTOUtil::convertToDTO).toList()).build();
    }

    public static TagDTO convertToDTO(Tag tag) {

        return TagDTO.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public static UserDTO convertToDTO(User user) {

        return UserDTO.builder().id(user.getId()).email(user.getEmail()).build();
    }

    public static OrderDTO convertToDTO(Order order) {

        return OrderDTO.builder().userId(order.getUser().getId()).giftCertificateId(order.getGiftCertificate().getId())
                .cost(order.getCost()).timestamp(order.getTimestamp()).build();
    }

    public static GiftCertificate convertToEntity(GiftCertificateDTO gcDTO) {

        GiftCertificate result =
                GiftCertificate.builder().id(gcDTO.getId()).name(gcDTO.getName()).description(gcDTO.getDescription())
                        .price(gcDTO.getPrice()).duration(gcDTO.getDuration()).build();
        if (gcDTO.getTags() != null) {
            result.setTags(gcDTO.getTags().stream().map(DTOUtil::convertToEntity).toList());
        }

        return result;
    }

    public static Tag convertToEntity(TagDTO tagDTO) {

        return Tag.builder().id(tagDTO.getId()).name(tagDTO.getName()).build();
    }
}

package com.epam.esm.util_service;

import com.epam.esm.dto.*;
import com.epam.esm.entity.*;

/**
 * Class for converting DTOs to entities and vice versa.
 *
 * @author Danylo Proshyn
 */

public class DTOUtil {

    public static GiftCertificateDTO convertToDTO(GiftCertificate gc) {

        return GiftCertificateDTO.builder()
                                 .id(gc.getId())
                                 .name(gc.getName())
                                 .description(gc.getDescription())
                                 .price(gc.getPrice())
                                 .duration(gc.getDuration())
                                 .createdDate(gc.getCreatedDate())
                                 .lastUpdatedDate(gc.getLastModifiedDate())
                                 .tags(gc.getTags().stream().map(DTOUtil::convertToDTO).toList())
                                 .build();
    }

    public static TagDTO convertToDTO(Tag tag) {

        return new TagDTO(tag.getName());
    }

    public static UserDTO convertToDTO(User user) {

        return new UserDTO(user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName(),
                ProviderName.valueOf(user.getProvider().getName().toUpperCase()));
    }

    public static OrderDTO convertToDTO(Order order) {

        return new OrderDTO(order.getId(), order.getCost(), order.getCreatedDate(),
                DTOUtil.convertToDTO(order.getUser()),
                order.getGiftCertificates().stream().map(DTOUtil::convertToDTO).toList());
    }

    public static InvalidatedTokenDTO convertToDTO(InvalidatedToken invToken) {

        return new InvalidatedTokenDTO(invToken.getToken(), invToken.getExpiryDate());
    }

    public static GiftCertificate convertToEntity(GiftCertificateDTO gcDTO) {

        GiftCertificate result =
                GiftCertificate.builder()
                               .id(gcDTO.getId())
                               .name(gcDTO.getName())
                               .description(gcDTO.getDescription())
                               .price(gcDTO.getPrice())
                               .duration(gcDTO.getDuration())
                               .createdDate(gcDTO.getCreatedDate())
                               .lastModifiedDate(gcDTO.getLastUpdatedDate())
                               .build();
        if (gcDTO.getTags() != null) {
            result.setTags(gcDTO.getTags().stream().map(DTOUtil::convertToEntity).toList());
        }

        return result;
    }

    public static Tag convertToEntity(TagDTO tagDTO) {

        return Tag.builder()
                  .name(tagDTO.getName())
                  .build();
    }

    public static User convertToEntity(UserDTO userDTO) {

        return User.builder()
                   .email(userDTO.getEmail())
                   .username(userDTO.getUsername())
                   .firstName(userDTO.getFirstName())
                   .lastName(userDTO.getLastName())
                   .build();
    }

    public static InvalidatedToken convertToEntity(InvalidatedTokenDTO invToken) {

        return InvalidatedToken.builder()
                               .token(invToken.getToken())
                               .expiryDate(invToken.getExpiryDate())
                               .build();
    }
}

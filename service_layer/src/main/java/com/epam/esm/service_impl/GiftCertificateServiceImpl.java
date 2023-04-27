package com.epam.esm.service_impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceAlreadyExists;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util_service.DTOUtil;
import com.epam.esm.util_service.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of service interface {@link com.epam.esm.service.GiftCertificateService}.
 *
 * @author Danylo Proshyn
 */

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository gcRepository;
    private final TagRepository             tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository gcRepository, TagRepository tagRepository) {

        this.gcRepository  = gcRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ResourceAlreadyExists.class)
    public void addGiftCertificate(GiftCertificateDTO gcDTO) throws ResourceAlreadyExists {

        GiftCertificate gc = DTOUtil.convertToEntity(gcDTO);
        gc.setCreateDate(LocalDateTime.now());
        gc.setLastUpdateDate(LocalDateTime.now());

        try {
            gcRepository.insertEntity(gc);
        } catch (DataAccessException e) {
//              transactionManager.rollback(TransactionAspectSupport.currentTransactionStatus());
//              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new ResourceAlreadyExists();
        }

        if (gcDTO.getTags() != null) {
            for (TagDTO tag : gcDTO.getTags()) {

                if (tagRepository.getEntity(tag.getId()).isEmpty()) {
                    tagRepository.insertEntity(DTOUtil.convertToEntity(tag));
                }

                gcRepository.addTagToEntity(gcDTO.getId(), tag.getId());
            }
        }
    }

    @Override
    public Optional<GiftCertificateDTO> getGiftCertificate(long id) {

        Optional<GiftCertificate> gc = gcRepository.getEntity(id);

        return gc.map(
                giftCertificate -> DTOUtil.convertToDTO(giftCertificate, tagRepository.getAllByGiftCertificate(id)));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ResourceAlreadyExists.class)
    public void updateGiftCertificate(GiftCertificateDTO gcDTO) throws ResourceAlreadyExists {

        GiftCertificate gc = gcRepository.getEntity(gcDTO.getId()).orElseThrow(ResourceAlreadyExists::new);
        if (gcDTO.getName() != null) {
            gc.setName(gcDTO.getName());
        }
        if (gcDTO.getDescription() != null) {
            gc.setDescription(gcDTO.getDescription());
        }
        if (gcDTO.getPrice() != null) {
            gc.setPrice(gcDTO.getPrice());
        }
        if (gcDTO.getDuration() != null) {
            gc.setDuration(gcDTO.getDuration());
        }
        gc.setLastUpdateDate(LocalDateTime.now());

        gcRepository.updateEntity(gc);

        gcRepository.deleteAllTagsForEntity(gc.getId());
        for (TagDTO tag : gcDTO.getTags()) {
            if (tagRepository.getEntity(gc.getId()).isEmpty()) {
                tagRepository.insertEntity(DTOUtil.convertToEntity(tag));
            }

            gcRepository.addTagToEntity(gc.getId(), tag.getId());
        }
    }

    @Override
    public boolean deleteGiftCertificate(long id) {

        return gcRepository.deleteEntity(id) != 0;
    }

    @Override
    public List<GiftCertificateDTO> getAll(
            Optional<String> tagName, Optional<String> namePart, Optional<String> descriptionPart,
            Optional<Order> nameOrder, Optional<Order> createDateOrder) {

        return gcRepository.getAll(tagName, namePart, descriptionPart, nameOrder.map(Enum::name),
                        createDateOrder.map(Enum::name)).stream()
                .map(gc -> DTOUtil.convertToDTO(gc, tagRepository.getAllByGiftCertificate(gc.getId()))).toList();
    }
}

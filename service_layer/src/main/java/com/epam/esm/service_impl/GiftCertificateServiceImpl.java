package com.epam.esm.service_impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util_service.DTOUtil;
import com.epam.esm.util_service.SortOrder;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of service interface {@link GiftCertificateService}.
 *
 * @author Danylo Proshyn
 */

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository gcRepository;
    private final TagRepository             tagRepository;

    private final Logger logger = LogManager.getLogger(GiftCertificateServiceImpl.class);

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository gcRepository, TagRepository tagRepository) {

        this.gcRepository  = gcRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED,
                   rollbackFor = TransactionFailException.class)
    public GiftCertificateDTO addGiftCertificate(@Valid GiftCertificateDTO gcDTO) throws TransactionFailException {

        GiftCertificate gc = DTOUtil.convertToEntity(gcDTO);
        GiftCertificateDTO addedGiftCertificate;
        try {
            addedGiftCertificate = DTOUtil.convertToDTO(gcRepository.insertEntity(gc));
        } catch (Exception e) {
//              transactionManager.rollback(TransactionAspectSupport.currentTransactionStatus());
//              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new TransactionFailException();
        }

        return addedGiftCertificate;
    }

    @Override
    public Optional<GiftCertificateDTO> getGiftCertificate(long id) {

        Optional<GiftCertificate> gc = gcRepository.getEntity(id);

        return gc.map(DTOUtil::convertToDTO);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED,
                   rollbackFor = TransactionFailException.class)
    public GiftCertificateDTO updateGiftCertificate(@Valid GiftCertificateDTO gcDTO) throws TransactionFailException {

        GiftCertificateDTO updatedGiftCertificate;

        try {
            GiftCertificate oldGc = gcRepository.getEntity(gcDTO.getId()).get();
            GiftCertificate newGc = DTOUtil.convertToEntity(gcDTO);
            newGc.setActive(oldGc.isActive());

            for (Tag tag : newGc.getTags()) {
                if (tagRepository.getEntity(newGc.getId()).isEmpty()) {
                    tagRepository.insertEntity(tag);
                }
            }

            updatedGiftCertificate = DTOUtil.convertToDTO(gcRepository.updateEntity(newGc));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransactionFailException();
        }

        return updatedGiftCertificate;
    }

    @Override
    public boolean deleteGiftCertificate(long id) {

        try {
            gcRepository.deleteEntity(id);
        } catch (Exception e) {
            logger.error(e);

            return false;
        }

        return true;
    }

    @Override
    public List<GiftCertificateDTO> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<SortOrder> nameOrder, Optional<SortOrder> createDateOrder) {

        return gcRepository.getAll(page, total, tagNames, namePart, descriptionPart, nameOrder.map(Enum::name),
                createDateOrder.map(Enum::name)).stream().map(DTOUtil::convertToDTO).toList();
    }
}

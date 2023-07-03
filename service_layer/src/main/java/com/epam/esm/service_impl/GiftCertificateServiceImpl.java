package com.epam.esm.service_impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util_service.DTOUtil;
import com.epam.esm.util_service.SortOrder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of service interface {@link GiftCertificateService}.
 *
 * @author Danylo Proshyn
 */

@Service
@Validated
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository gcRepository;
    private final TagRepository             tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository gcRepository, TagRepository tagRepository) {

        this.gcRepository  = gcRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public GiftCertificateDTO addGiftCertificate(@Valid GiftCertificateDTO gcDTO) {

        return saveGiftCertificate(DTOUtil.convertToEntity(gcDTO));
    }

    @Override
    public Optional<GiftCertificateDTO> getGiftCertificate(long id) {

        return gcRepository.findById(id).map(DTOUtil::convertToDTO);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED,
                   rollbackFor = TransactionFailException.class)
    public GiftCertificateDTO updateGiftCertificate(@Valid GiftCertificateDTO gcDTO) throws TransactionFailException {

        Optional<GiftCertificate> oldGc = gcRepository.findById(gcDTO.getId());
        if (oldGc.isEmpty()) {
            throw new TransactionFailException();
        }

        GiftCertificate newGc = DTOUtil.convertToEntity(gcDTO);
        newGc.setCreatedDate(oldGc.get().getCreatedDate());

        return saveGiftCertificate(newGc);
    }

    private GiftCertificateDTO saveGiftCertificate(GiftCertificate gc) {

        for (Tag tag : gc.getTags()) {
            Optional<Tag> tagInDB = tagRepository.findByName(tag.getName());
            if (tagInDB.isEmpty()) {
                tag.setId(tagRepository.save(tag).getId());
            } else {
                tag.setId(tagInDB.get().getId());
            }
        }

        return DTOUtil.convertToDTO(gcRepository.save(gc));
    }

    @Override
    public boolean deleteGiftCertificate(long id) {

        if (gcRepository.findById(id).isEmpty()) {
            return false;
        }
        gcRepository.deleteById(id);

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

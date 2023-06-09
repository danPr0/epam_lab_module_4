package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.util_service.SortOrder;

import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link GiftCertificateDTO}.
 *
 * @author Danylo Proshyn
 */

public interface GiftCertificateService {

    void addGiftCertificate(GiftCertificateDTO gc) throws TransactionFailException;

    Optional<GiftCertificateDTO> getGiftCertificate(long id);

    void updateGiftCertificate(GiftCertificateDTO gc) throws TransactionFailException;

    boolean deleteGiftCertificate(long id);

    List<GiftCertificateDTO> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<SortOrder> nameOrder, Optional<SortOrder> createDateOrder);
}

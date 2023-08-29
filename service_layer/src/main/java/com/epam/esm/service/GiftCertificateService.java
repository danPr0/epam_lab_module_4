package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.util_service.SortOrder;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link GiftCertificateDTO}.
 *
 * @author Danylo Proshyn
 */

public interface GiftCertificateService {

    GiftCertificateDTO addGiftCertificate(@Valid GiftCertificateDTO gc);

    Optional<GiftCertificateDTO> getGiftCertificate(long id);

    GiftCertificateDTO updateGiftCertificate(@Valid GiftCertificateDTO gc) throws TransactionFailException;

    boolean deleteGiftCertificate(long id);

    Pair<List<GiftCertificateDTO>, Integer> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> textFilter,
             Optional<SortOrder> nameOrder, Optional<SortOrder> createDateOrder);
}

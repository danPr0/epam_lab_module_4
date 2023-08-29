package service_test;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service_impl.GiftCertificateServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {GiftCertificateServiceImpl.class})
public class GiftCertificateServiceTest extends Mockito {

    @Autowired
    private GiftCertificateService gcService;

    @MockBean
    private GiftCertificateRepository gcRepository;
    @MockBean
    private TagRepository             tagRepository;

    private final GiftCertificate gc;

    private final GiftCertificateDTO gcDTO;

    private final Tag tag1;
    private final Tag tag2;

    private final TagDTO tag1DTO;
    private final TagDTO tag2DTO;

    {
        tag1 = new Tag(1L, "1");
        tag2 = new Tag(2L, "2");

        tag1DTO = new TagDTO("1");
        tag2DTO = new TagDTO("2");

        LocalDateTime createdDate      = LocalDateTime.now();
        LocalDateTime lastModifiedDate = LocalDateTime.now();

        gc = new GiftCertificate(1L, "1", "1", 1, 1, List.of(tag1, tag2));
        gc.setCreatedDate(createdDate);
        gc.setLastModifiedDate(lastModifiedDate);

        gcDTO = new GiftCertificateDTO(1L, "1", "1", 1.0, 1, createdDate, lastModifiedDate, List.of(tag1DTO, tag2DTO));
    }

    @Test
    public void testAddGiftCertificateSuccess() {

        GiftCertificate gcToAdd = gc.clone();
        gcToAdd.setId(null);
        gcToAdd.setCreatedDate(null);
        gcToAdd.setLastModifiedDate(null);

        GiftCertificateDTO gcDTOToAdd = gcDTO.clone();
        gcDTOToAdd.setId(null);
        gcDTOToAdd.setCreatedDate(null);
        gcDTOToAdd.setLastUpdatedDate(null);

        when(tagRepository.findByName(tag1DTO.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(Tag.builder().name(tag1DTO.getName()).build())).thenReturn(tag1);
        when(tagRepository.findByName(tag2DTO.getName())).thenReturn(Optional.of(tag2));
        when(gcRepository.save(gcToAdd)).thenReturn(gc);

        assertEquals(gcDTO, gcService.addGiftCertificate(gcDTOToAdd));
        verify(gcRepository).save(gcToAdd);
    }

    @Test
    public void testGetGiftCertificateSuccess() {

        when(gcRepository.findById(gc.getId())).thenReturn(Optional.of(gc));

        assertEquals(Optional.of(gcDTO), gcService.getGiftCertificate(gcDTO.getId()));
        verify(gcRepository).findById(gc.getId());
    }

    @Test
    public void testGetGiftCertificateFail() {

        when(gcRepository.findById(gc.getId())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), gcService.getGiftCertificate(gcDTO.getId()));
        verify(gcRepository).findById(gc.getId());
    }

    @Test
    public void testUpdateGiftCertificateSuccess() {

        GiftCertificate gcToUpdate = gc.clone();
        gcToUpdate.setLastModifiedDate(null);

        GiftCertificateDTO gcDTOToUpdate = gcDTO.clone();
        gcDTOToUpdate.setCreatedDate(null);
        gcDTOToUpdate.setLastUpdatedDate(null);

        when(gcRepository.findById(gcDTO.getId())).thenReturn(Optional.of(gc));
        when(tagRepository.findByName(tag1DTO.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(Tag.builder().name(tag1DTO.getName()).build())).thenReturn(tag1);
        when(tagRepository.findByName(tag2DTO.getName())).thenReturn(Optional.of(tag2));
        when(gcRepository.save(gcToUpdate)).thenReturn(gc);

        assertEquals(gcDTO, assertDoesNotThrow(() -> gcService.updateGiftCertificate(gcDTOToUpdate)));
        verify(gcRepository).save(gcToUpdate);
    }

    @Test
    public void testUpdateGiftCertificateFail() {

        when(gcRepository.findById(gcDTO.getId())).thenReturn(Optional.empty());

        assertThrows(TransactionFailException.class, () -> gcService.updateGiftCertificate(gcDTO));
    }

    @Test
    public void deleteGiftCertificateSuccess() {

        when(gcRepository.findById(gc.getId())).thenReturn(Optional.of(gc));
        doNothing().when(gcRepository).deleteById(gc.getId());

        assertTrue(gcService.deleteGiftCertificate(gcDTO.getId()));
        verify(gcRepository).deleteById(gc.getId());
    }

    @Test
    public void deleteGiftCertificateFail() {

        when(gcRepository.findById(gc.getId())).thenReturn(Optional.empty());

        assertFalse(gcService.deleteGiftCertificate(gcDTO.getId()));
    }

    @Test
    public void testGetAll() {

        int page = 1;
        int pageSize = 500;
        int totalCount = 1001;

        when(gcRepository.getAll(page, pageSize, Optional.of(List.of(tag1.getName(), tag2.getName())), Optional.empty(),
                Optional.empty(), Optional.empty())).thenReturn(
                new PageImpl<>(List.of(gc), Pageable.ofSize(pageSize), totalCount));

        assertEquals(Pair.of(List.of(gcDTO), 3),
                gcService.getAll(page, pageSize, Optional.of(List.of(tag1.getName(), tag2.getName())), Optional.empty(),
                        Optional.empty(), Optional.empty()));
        verify(gcRepository).getAll(page, pageSize, Optional.of(List.of(tag1.getName(), tag2.getName())),
                Optional.empty(), Optional.empty(), Optional.empty());
    }
}

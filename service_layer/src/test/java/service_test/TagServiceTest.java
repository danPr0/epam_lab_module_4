package service_test;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service_impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TagServiceImpl.class})
class TagServiceTest extends Mockito {

    @Autowired
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    private final Tag tag = Tag.builder().name("1").build();

    private final TagDTO tagDTO = new TagDTO("1");

    @Test
    void testAddTagSuccess() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(tag)).thenReturn(tag);

        assertTrue(tagService.addTag(tagDTO));
        verify(tagRepository).save(tag);
    }

    @Test
    void testAddTagFail() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.of(tag));

        assertFalse(tagService.addTag(tagDTO));
    }

    @Test
    void testGetTagSuccess() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.of(tag));

        assertEquals(Optional.of(tagDTO), tagService.getTag(tagDTO.getName()));
        verify(tagRepository).findByName(tagDTO.getName());
    }

    @Test
    void testGetTagFail() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), tagService.getTag(tagDTO.getName()));
        verify(tagRepository).findByName(tagDTO.getName());
    }

    @Test
    void testComplexQuerySuccess() {

        when(tagRepository.complexJPQLQuery()).thenReturn(Optional.of(tag));

        assertEquals(Optional.of(tagDTO), tagService.getMostPopularUserTag());
        verify(tagRepository).complexJPQLQuery();
    }

    @Test
    void testComplexQueryFail() {
        when(tagRepository.complexJPQLQuery()).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), tagService.getMostPopularUserTag());
        verify(tagRepository).complexJPQLQuery();
    }

    @Test
    void testDeleteTagSuccess() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).deleteByName(tagDTO.getName());

        assertTrue(tagService.deleteTag(tagDTO.getName()));
        verify(tagRepository).deleteByName(tagDTO.getName());
    }

    @Test
    void testDeleteTagFail() {

        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.empty());

        assertFalse(tagService.deleteTag(tagDTO.getName()));
    }
}

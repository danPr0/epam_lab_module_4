import com.epam.esm.RepositoryConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest(properties = {"spring.profiles.active=prod"}, classes = {RepositoryConfig.class})
////@ContextConfiguration(classes = DataSourceTestConfig.class)
////@TestPropertySource("classpath:datasource-dev.properties")
//public class ManagerTest {
//
//    @Autowired
//    private TagRepository tagRepository;
//
//    @Test
//    public void test() {
//        tagRepository.insertEntity(new Tag(1L, "1"));
//    }
//}

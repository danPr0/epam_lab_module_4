package repository_test;

//import DataSourceTestConfig;
//import repository_test.util.DbTestUtil;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static repository_test.util.DbTestUtil.gc1;

/**
 * This class is used for testing DAO repository {@link com.epam.esm.repository_impl.TagRepositoryImpl}.
 *
 * @author Danylo Proshyn
 */

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@ContextConfiguration(classes = {TagRepositoryImpl.class, DataSourceTestConfig.class})
////@Import(DataSourceTestConfig.class)
////@SpringBootTest(classes = {TagRepositoryImpl.class, DataSourceConfig.class})
////@SpringBootTest(classes = {TagRepositoryImpl.class, DataSourceTestConfig.class})
//@EnableJpaRepositories
////@SpringBootTest(classes = {TagRepositoryImpl.class, DataSourceTestConfig.class})
////@ComponentScan(basePackages = "com.epam.esm")
////@EnableAutoConfiguration
////@TestPropertySource("classpath:datasource-prod.properties")
////@ActiveProfiles("prod")
//public class TagRepositoryTest {
//
//    @Autowired
//    private TagRepository tagRepository;
//
////    private final JdbcTemplate jdbcTemplate;
//    private final DbTestUtil dbTestUtil = new DbTestUtil();
//
////    @Autowired
////    private EntityManager em;
//
////    @Autowired
////    public TagRepositoryTest(TagRepository tagRepository) {
////
////        this.tagRepository = tagRepository;
//////        this.jdbcTemplate  = jdbcTemplate;
////
////        dbTestUtil = new DbTestUtil();
////    }
//
////    @BeforeEach
////    public void createAndFillTable() {
////
////        dbTestUtil.createTables(jdbcTemplate);
////        dbTestUtil.fillTables(jdbcTemplate);
////    }
////
////    @AfterEach
////    public void dropTable() {
////
////        dbTestUtil.dropTables(jdbcTemplate);
////    }
//
////    public int countRows() {
////
////        //noinspection DataFlowIssue
////        return jdbcTemplate.queryForObject("select count(*) from gift_certificates", Integer.class);
////    }
//
//    @Test
////    @Transactional
//    public void testGet() {
//
//        dbTestUtil.createEntities();
////        em.persist(gc1);
//
//        assertEquals(DbTestUtil.tag1, tagRepository.getEntity(DbTestUtil.tag1.getId()).get());
//
//        assertTrue(tagRepository.getEntity(DbTestUtil.tag3Spare.getId()).isEmpty());
//    }
////
////    @Test
////    public void testInsert() {
////
////        tagRepository.insertEntity(DbTestUtil.tag3Spare);
////
////        assertEquals(5, countRows());
////        assertEquals(DbTestUtil.tag3Spare, tagRepository.getEntity(DbTestUtil.tag3Spare.getId()).get());
////
////        assertThrows(Exception.class, () -> tagRepository.insertEntity(DbTestUtil.tag3Spare));
////        assertEquals(5, countRows());
////    }
////
////    @Test
////    public void testDelete() {
////
////        assertDoesNotThrow(() -> tagRepository.deleteEntity(DbTestUtil.tag2.getId()));
////        assertEquals(3, countRows());
////
////        assertTrue(tagRepository.getEntity(DbTestUtil.tag2.getId()).isEmpty());
////        assertThrows(Exception.class, () -> tagRepository.deleteEntity(DbTestUtil.tag2.getId()));
////    }
//
////    @Test
////    public void testGetAllByGiftCertificate() {
////
////        List<Tag> tags = tagRepository.getAllByGiftCertificate(DbTestUtil.gc1.getId());
////
////        assertArrayEquals(new Long[]{DbTestUtil.tag1.getId(), DbTestUtil.tag11.getId()},
////                tags.stream().map(Tag::getId).sorted().toArray());
////
////        assertEquals(0, tagRepository.getAllByGiftCertificate(DbTestUtil.gc3Spare.getId()).size());
////    }
//}
//

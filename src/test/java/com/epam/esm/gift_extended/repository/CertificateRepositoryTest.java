//package com.epam.esm.gift_extended.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.Date;
//
//import javax.persistence.EntityManager;
//import javax.sql.DataSource;
//
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.epam.esm.gift_extended.entity.Certificate;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@EnableTransactionManagement
//class CertificateRepositoryTest {
//
//    private static Certificate testCert = new Certificate();
////store embeded db
//    private boolean isConfigured = false;
//
//    static {
//        testCert.setUpdateTime(new Date());
//        testCert.setCreationTime(new Date());
//        testCert.setPrice(1.0f);
//        testCert.setDescription("Test description");
//        testCert.setName("Test name");
//        testCert.setDuration(1);
//    }
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Autowired
//    private CertificateRepository certificateRepository;
//
//    @Autowired
//    private UserRepository userRepository;
///*
//    @Transactional
//    @AfterEach
//    @DatabaseSetup("db.xml")
//    public void deleteTestCert() {
//        List<Certificate> certificates = certificateRepository.findByName("Test name");
//        if (certificates.size() != 0) {
//            Certificate certificate = certificateRepository.findByName("Test name").get(0);
//            certificateRepository.delete(certificate);
//        }
//    }
//
//    @Transactional
//    @Test
//    void injectedComponentsAreNotNull() {
//        assertNotNull(dataSource);
//        assertNotNull(jdbcTemplate);
//        assertNotNull(entityManager);
//        assertNotNull(certificateRepository);
//    }
//
//    @Transactional
//    @Test
//    void add() {
//        List<Certificate> certificates = certificateRepository.findByName("Test name");
//        assertEquals(0, certificates.size());
//
//        certificateRepository.save(testCert);
//
//        List<Certificate> founded = certificateRepository.findByName("Test name");
//        assertNotEquals(0, founded.size());
//
//    }
//
//    @Transactional
//    @Test
//    void delete() {
//
//        certificateRepository.save(testCert);
//        Certificate certificates = certificateRepository.findByName("Test name").get(0);
//        assertNotEquals(null, certificates);
//
//        certificateRepository.delete(testCert);
//        List<Certificate> certificatesAfterDelete = certificateRepository.findByName("Test name");
//        assertEquals(0, certificatesAfterDelete.size());
//
//    }
//
//    @Transactional
//    @Test
//    void findTagById() {
//
//        certificateRepository.save(testCert);
//        Certificate certificate = certificateRepository.findByName("Test name").get(0);
//        Certificate certificateFoundedById = certificateRepository.findById(certificate.getId()).get();
//        assertEquals(certificate, certificateFoundedById);
//    }
//
//    @Transactional
//    @Test
//    void getAll() {
//        List<Certificate> list = new ArrayList<>();
//        certificateRepository.findAll().forEach(list::add);
//        assertEquals(3, list.size());
//    }
//
//    @Transactional
//    @Test
//    void update() {
//
//        certificateRepository.save(testCert);
//
//        Certificate certificateFromDB = certificateRepository.findByName("Test name").get(0);
//        certificateFromDB.setDuration(2);
//        certificateRepository.save(certificateFromDB);
//        Certificate certificateFromDB2 = certificateRepository.findById(certificateFromDB.getId()).get();
//        assertEquals(2, certificateFromDB2.getDuration());
//    }
//
//    @Transactional
//    @Test
//    void findUserCertificates() {
//        User user = userRepository.findByName("user1").get();
//        List<Certificate> certificates = certificateRepository.findUserCertificates(user.getId());
//        assertEquals(2, certificates.size());
//    }
//
//
//    @Transactional
//    @Test
//    void findDistinctByDescriptionContainingAndNameContaining() {
//        List<Certificate> certificates = new ArrayList<>(
//                certificateRepository.findDistinctByDescriptionContainingAndNameContaining("t", "not exist"));
//        assertEquals(3, certificates.size());
//        certificates = new ArrayList<>(
//                certificateRepository.findDistinctByDescriptionContainingAndNameContaining("t1", "not exist"));
//        assertEquals(1, certificates.size());
//    }*/
//}
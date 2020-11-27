package com.epam.esm.gift_extended.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

@ExtendWith(SpringExtension.class)
@EnableTransactionManagement
@SpringBootTest
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepositoryImpl certificateRepository;

    @Autowired
    private TagRepositoryImpl tagRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void autoInjectCorrectly() {
        assertNotNull(certificateRepository);
        assertNotNull(dataSource);
    }

    @Test
    void findByName() {
        Optional<Certificate> Certificate = certificateRepository.findByName("spa massage");
        assertEquals(6, Certificate.get().getId());

        Optional<Certificate> Certificate2 = certificateRepository.findByName("none");
        assertFalse(Certificate2.isPresent());
    }

    @Test
    void findAll() {
        List<Certificate> Certificates = certificateRepository.findAll();
        List<Certificate> CertificateList = new ArrayList<Certificate>();
        Certificates.forEach(CertificateList::add);
        assertEquals(4, CertificateList.size());
    }

    @Test
    void save() {
        Certificate Certificate = new Certificate();
        Certificate.setName("newCertificate");
        certificateRepository.save(Certificate);
        Optional<Certificate> Certificates = certificateRepository.findByName("newCertificate");

        assertTrue(Certificates.isPresent());
        Iterable<Certificate> newCertificates = certificateRepository.findAll();
        List<Certificate> CertificateList = new ArrayList<Certificate>();
        newCertificates.forEach(CertificateList::add);
        assertEquals(5, CertificateList.size());
        certificateRepository.delete(Certificate);
        Iterable<Certificate> Certificatess = certificateRepository.findAll();

    }

    @Test
    void findById() {
        List<Certificate> Certificates = certificateRepository.findAll();
        Optional<Certificate> certificate = certificateRepository.findById(7);
        assertEquals(7, certificate.get().getId());

        Optional<Certificate> certificate2 = certificateRepository.findById(100);
        assertFalse(certificate2.isPresent());
    }

    @Test
    void count() {
        long count = certificateRepository.count();
        assertEquals(4, count);
    }

    @Test
    void delete() {
        Optional<Certificate> certificate = certificateRepository.findById(2);
        certificateRepository.delete(certificate.get());
        Iterable<Certificate> Certificates = certificateRepository.findAll();
        List<Certificate> CertificateList = new ArrayList<Certificate>();
        Certificates.forEach(CertificateList::add);
        assertEquals(3, CertificateList.size());
        certificateRepository.save(certificate.get());
    }

    @Test
    void isExist() {
        Optional<Certificate> existCertificateOpt = certificateRepository.findById(7);
        assertTrue(existCertificateOpt.isPresent());
        Certificate existCertificate = existCertificateOpt.get();
        boolean answer1 = certificateRepository.isExist(existCertificate);
        assertTrue(answer1);

        Certificate notExistCertificate = new Certificate();
        existCertificate.setName("not existing name");
        boolean answer2 = certificateRepository.isExist(notExistCertificate);
        assertFalse(answer2);

    }

    @Test
    void findUserCertificates() {
        List<Certificate> certificates=certificateRepository.findUserCertificates(4);
        assertEquals(3, certificates.size());
    }

    @Test
    void findByContainsAllCertificateNames() {
        Tag tag1=tagRepository.findById(9).get();
        Tag tag2=tagRepository.findById(10).get();
        List<Certificate> certificates = certificateRepository.findByContainsAllTagNames(List.of(tag1,tag2));
        assertEquals(1,certificates.size());
        assertEquals(5,certificates.get(0).getId());
        List<Certificate> certificates2 = certificateRepository.findByContainsAllTagNames(List.of(tag2));
        assertEquals(2,certificates2.size());
    }

    @Test
    void findCertificateByHolderAndCertificates() {
        Tag tag1=tagRepository.findById(9).get();
        User user = userRepository.findById(4).get();
        List<Certificate> certificates =certificateRepository.findCertificateByHolderAndTag(user,tag1);
        assertEquals(1,certificates.size());
    }

    @Test
    void findDistinctByDescriptionContainingAndNameContaining() {
        List<Certificate> certificates =certificateRepository.findDistinctByDescriptionContainingAndNameContaining("par","par");
        assertEquals(2,certificates.size());

        List<Certificate> certificates2 =certificateRepository.findDistinctByDescriptionContainingAndNameContaining("guc","guc");
        assertEquals(1,certificates2.size());
    }
}
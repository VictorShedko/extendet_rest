package com.epam.esm.gift_extended.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.repository.CertificateRepositoryImpl;

class CertificateServiceTest {

    private static User user = new User();
    private static Certificate testCert1 = new Certificate();
    private static Certificate testCert2 = new Certificate();
    private static Tag tag1 = new Tag();
    private static List<Certificate> allCerts = List.of(testCert1, testCert2);

    static {
        testCert1.setId(1);
        testCert1.setName("test1");
        testCert1.setDescription("test desc 1");
        testCert1.setPrice(1.0f);
        testCert1.setCreationTime(new Date());
        testCert1.setUpdateTime(new Date());
        testCert1.setDuration(1);

        testCert2.setId(2);
        testCert2.setName("test2");
        testCert2.setDescription("test desc 2");
        testCert2.setPrice(2.0f);
        testCert2.setCreationTime(new Date());
        testCert2.setUpdateTime(new Date());
        testCert2.setDuration(2);

    }

    @InjectMocks
    private CertificateService service = new CertificateService();

    @Mock
    private CertificateRepositoryImpl certificateRepository;

    @Mock
    private TagService tagService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
/*
    @Test
    void all() {
        List<Certificate> certificates = List.of(testCert1, testCert2);
        Mockito.when(certificateRepository.findAll()).thenReturn(certificates);

        Iterable<Certificate> resultCerts = service.all();

        assertEquals(resultCerts, certificates);
    }

    @Test
    void searchByAnyString() {
        Mockito.when(certificateRepository.findDistinctByDescriptionContainingAndNameContaining("te", "te"))
                .thenReturn(allCerts);

        Iterable<Certificate> certificates = service.searchByAnyString("te");
        assertEquals(certificates, allCerts);
    }

    @Test
    void searchByTag() {
        Mockito.when(tagService.findByName("test")).thenReturn(Optional.of(tag1));
        Mockito.when(certificateRepository.findDistinctByTags(tag1)).thenReturn(List.of(testCert1, testCert2));
        service.searchByTag("test");
    }

    @Test
    void searchByListOfTagNames() {
        List<String> strings=List.of("1","2","3");
        service.searchByListOfTagNames(strings);
    }

    @Test
    void searchByUserAndTag() {
    }

    @Test
    void one() {
        Mockito.when(certificateRepository.findById(1)).thenReturn(Optional.of(testCert1));
        Mockito.when(certificateRepository.findById(2)).thenReturn(Optional.of(testCert2));
        Certificate cert1 = service.findById(1);
        Certificate cert2 = service.findById(2);
        assertEquals(testCert1, cert1);
        assertEquals(testCert2, cert2);
    }

    @Test
    void save() {
        service.save(testCert1);
        Mockito.verify(certificateRepository).save(testCert1);
    }

    @Test
    void update() {
        Mockito.when(certificateRepository.findById(1)).thenReturn(Optional.of(testCert1));
        service.update(testCert1);
        Mockito.verify(certificateRepository).save(testCert2);

    }

    @Test
    void delete() {
        Mockito.when(certificateRepository.findById(1)).thenReturn(Optional.of(testCert1));
        service.delete(1);
        Mockito.verify(certificateRepository).delete(Mockito.any());
    }

    @Test
    void attachTag() {
        service.attachTag(1, 2);
    }

    @Test
    void detachTag() {
    }

    @Test
    void allWithPagination() {
    }

    @Test
    void findCertificatesByUser() {
    }

    @Test
    void setHolder() {
        service.setHolder(1, user);
    }

 */
}
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
    private CertificateService service ;

    @Mock
    private CertificateRepositoryImpl certificateRepository;

    @Mock
    private TagService tagService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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
    void searchByListOfTagNames() {
        Tag t=new Tag(1,"1");
        Mockito.when(tagService.findByName(Mockito.any())).thenReturn(Optional.of(t));
        Mockito.when(certificateRepository.findByContainsAllTagNames(Mockito.any())).thenReturn(List.of(testCert1));
        List<String> strings=List.of("1","2","3");
        List<Certificate> certificates=service.searchByListOfTagNames(strings);
        assertEquals(1,certificates.size());
        assertEquals(testCert1,certificates.get(0));
    }

    @Test
    void searchByUserAndTag() {
        Mockito.when(tagService.findById(1)).thenReturn(tag1);
        Mockito.when(certificateRepository.findCertificateByHolderAndTag(user,tag1)).thenReturn(List.of(testCert1));
        Mockito.when(userService.findById(2)).thenReturn(user);
        List<Certificate> certificates=service.searchByUserAndTag(1,2);
        assertEquals(certificates.get(0),testCert1);

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
        Certificate certificate=new Certificate();
        certificate.setId(testCert1.getId());
        Mockito.when(certificateRepository.findById(testCert1.getId())).thenReturn(Optional.of(certificate));
        service.update(testCert1);
        assertEquals(certificate.getCreationTime(),testCert1.getCreationTime());
        assertEquals(certificate.getHolder(),testCert1.getHolder());
        assertEquals(certificate.getDuration(),testCert1.getDuration());
        assertEquals(certificate.getPrice(),testCert1.getPrice());
    }

    @Test
    void delete() {
        Mockito.when(certificateRepository.findById(1)).thenReturn(Optional.of(testCert1));
        service.delete(1);
        Mockito.verify(certificateRepository).delete(Mockito.any());
    }

    @Test
    void attachTag() {
        Certificate certificate=new Certificate();
        Mockito.when(tagService.findById(1)).thenReturn(tag1);
        Mockito.when(certificateRepository.findById(2)).thenReturn(Optional.of(certificate));
        service.attachTag(1, 2);
        assertEquals(1,certificate.getTags().size());
        assertEquals(tag1,certificate.getTags().get(0));
    }

    @Test
    void detachTag() {
        Certificate certificate=new Certificate();
        certificate.attachTag(tag1);
        Mockito.when(tagService.findById(1)).thenReturn(tag1);
        Mockito.when(certificateRepository.findById(2)).thenReturn(Optional.of(certificate));
        service.detachTag(1, 2);
        assertEquals(0,certificate.getTags().size());

    }


    @Test
    void findCertificatesByUser() {
        List list=List.of(testCert1);
        Mockito.when(certificateRepository.findUserCertificates(1)).thenReturn(list);
        assertEquals(list,service.findCertificatesByUser(1));

    }

    @Test
    void setHolder() {
        Certificate certificate=new Certificate();
        Mockito.when(certificateRepository.findById(1)).thenReturn(Optional.of(certificate));
        service.setHolder(1, user);
        assertEquals(user,certificate.getHolder());
    }

}
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
import com.epam.esm.gift_extended.repository.TagRepositoryImpl;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

class TagServiceTest {
    private static User user = new User();
    private static Certificate testCert1 = new Certificate();
    private static Certificate testCert2 = new Certificate();
    private static Tag tag1 = new Tag();
    private static Tag tag2 = new Tag();
    private static List<Certificate> allCerts = List.of(testCert1, testCert2);
    public int page=0;
    public int size=10;
    public String sort="";

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

        tag1.setName("tag1");
        tag2.setName("tag2");
    }

    @InjectMocks
    private TagService service ;

    @Mock
    private TagRepositoryImpl tagRepository;

    @Mock
    private CertificateService certificateService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void all() {
        List<Tag> list=List.of(tag1,tag2);
        Mockito.when(tagRepository.findAll()).thenReturn(list);
        assertEquals(list,service.all());
    }


    @Test
    void findById() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        assertEquals(tag1,service.findById(1));
    }

    @Test
    void add() {
        service.saveFromString("test");
        Tag newTag = new Tag();
        newTag.setName("test");
        Mockito.verify(tagRepository).save(newTag);
    }

    @Test
    void save() {
        service.save(tag1);
        Mockito.verify(tagRepository).save(tag1);
    }

    @Test
    void delete() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        service.delete(1);
        Mockito.verify(tagRepository).delete(tag1);
    }

    @Test
    void tags() {
        Certificate certificate=new Certificate();
        List<Tag> tags=List.of(tag1,tag2);
        certificate.setTags(tags);
        Mockito.when(tagRepository.findByCert(1,Mockito.any())).thenReturn(tags);
        assertEquals(service.tags(1, page, size, sort),tags);
    }

    @Test
    void findByName() {
        Mockito.when(tagRepository.findByName("test")).thenReturn(Optional.ofNullable(tag1));
        assertEquals(tag1,service.findByName("tag1").get());
    }


    @Test
    void countEntities() {
        Mockito.when(tagRepository.count()).thenReturn(3L);
        assertEquals(3,service.countEntities());
    }

    @Test
    void isExist() {
        Mockito.when(tagRepository.isExist(tag1)).thenReturn(true);
        assertTrue(tagRepository.isExist(tag1));
    }
}
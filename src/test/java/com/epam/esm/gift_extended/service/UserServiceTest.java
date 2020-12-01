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
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.repository.UserRepository;

class UserServiceTest {
    private static User user = new User();
    private static Certificate testCert1 = new Certificate();
    private static Certificate testCert2 = new Certificate();
    private static User user1 = new User();
    private static User user2 = new User();
    
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

        user1.setName("user1");
        user2.setName("user2");
    }

    @InjectMocks
    private UserService service ;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CertificateService certificateService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

   

    @Test
    void makeOrder() {
        Certificate certificate=new Certificate();
        Mockito.when(certificateService.findById(1)).thenReturn(certificate);
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user1));
        //service.makeOrder(1,2);
       // assertEquals(user1,certificate.getHolder());
    }


    @Test
    void findByPartOfName() {
        List<User> list=List.of(user1,user2);
        Mockito.when(userRepository.findByNameContains("test")).thenReturn(list);
        assertEquals(list,service.findByPartOfName("test", page, size, sort));
    }

    @Test
    void all() {
        List<User> list=List.of(user1,user2);
        Mockito.when(userRepository.findAll()).thenReturn(list);
        assertEquals(list,service.all());
    }


    @Test
    void findById() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        assertEquals(user1,service.findById(1));
    }

    @Test
    void save() {
        service.save(user1);
        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void delete() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        service.delete(1);
        Mockito.verify(userRepository).delete(user1);
    }



    @Test
    void findByName() {
        Mockito.when(userRepository.findByName("test")).thenReturn(Optional.ofNullable(user1));
        assertEquals(user1,service.findByName("user1"));
    }


    @Test
    void countEntities() {
        Mockito.when(userRepository.count()).thenReturn(3L);
        assertEquals(3,service.countEntities());
    }

    @Test
    void isExist() {
        Mockito.when(userRepository.isExist(user1)).thenReturn(true);
        assertTrue(userRepository.isExist(user1));
    }
}
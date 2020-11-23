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

import com.epam.esm.gift_extended.entity.User;

@ExtendWith(SpringExtension.class)
@EnableTransactionManagement
@SpringBootTest
class UserRepositoryImplTest {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void autoInjectCorrectly() {
        assertNotNull(userRepository);
        assertNotNull(dataSource);
    }

    @Test
    void findByName() {
        Optional<User> user = userRepository.findByName("Borya");
        assertEquals(1,user.get().getId());

        Optional<User> user2 = userRepository.findByName("none");
        assertFalse(user2.isPresent());
    }

    @Test
    void findAll() {
        Iterable<User> users= userRepository.findAll();
        List<User> userList=new ArrayList<User>();
        users.forEach(userList::add);
        assertEquals(4,userList.size());
    }

    @Test
    void save() {
        User user=new User();
        user.setName("newuser");
        userRepository.save(user);
        Optional<User> users= userRepository.findByName("newuser");

        assertTrue(users.isPresent());
        Iterable<User> newusers= userRepository.findAll();
        List<User> userList=new ArrayList<User>();
        newusers.forEach(userList::add);
        assertEquals(5,userList.size());
        userRepository.delete(user);
        Iterable<User> users2= userRepository.findAll();

    }

    @Test
    void findById() {

        Optional<User> user = userRepository.findById(4);
        assertEquals(4,user.get().getId());
        assertEquals("Liza",user.get().getName());
        Optional<User> user2 = userRepository.findById(100);
        assertFalse(user2.isPresent());
    }


    @Test
    void count() {
        long count= userRepository.count();
        assertEquals(4,count);
    }

    @Test
    void delete() {
        Optional<User> user= userRepository.findById(3);
        userRepository.delete(user.get());
        Iterable<User> users= userRepository.findAll();
        List<User> userList=new ArrayList<User>();
        users.forEach(userList::add);
        assertEquals(3,userList.size());
        userRepository.save(user.get());
    }

    @Test
    void isExist() {
        Optional<User> existCertificateOpt = userRepository.findById(4);
        assertTrue(existCertificateOpt.isPresent());
        User existCertificate = existCertificateOpt.get();
        boolean answer1 = userRepository.isExist(existCertificate);
        assertTrue(answer1);

        User notExistCertificate = new User();
        existCertificate.setName("not existing name");
        boolean answer2 = userRepository.isExist(notExistCertificate);
        assertFalse(answer2);

    }
}
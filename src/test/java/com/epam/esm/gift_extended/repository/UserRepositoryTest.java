package com.epam.esm.gift_extended.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;
    private boolean isConfigured;

    @BeforeEach
    public void cofigure(){
        if(!isConfigured){
            RepositoryTestUtil.configure(tagRepository,userRepository, certificateRepository);
            isConfigured=true;
        }
    }



    @Test
    void findRichestByOrderPriceSum() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findByNameContains() {
    }
}
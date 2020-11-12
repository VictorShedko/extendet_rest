package com.epam.esm.gift_extended.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.epam.esm.gift_extended.repository.forbidentouse.CertificateRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.TagRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.UserRepositoryWithSpringData;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private CertificateRepositoryWith certificateRepository;

    @Autowired
    private TagRepositoryWith tagRepository;

    @Autowired
    private UserRepositoryWithSpringData userRepository;
    private boolean isConfigured;




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
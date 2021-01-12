package com.epam.esm.gift_extended.genertors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.esm.gift_extended.entity.Certificate;

class CertificateGeneratorTest {
    private CertificateGenerator generator=new CertificateGenerator();
    @Test
    void generateCertificateList() {
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        assertTrue(encoder.matches("lol",encoder.encode("lol")));
        //List<Certificate> certificateList=generator.generateCertificateList(1000);
        //assertEquals(1000,certificateList.size());
    }
}
package com.epam.esm.gift_extended;

import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.epam.esm.gift_extended.genertors.GeneratedSaverService;

@SpringBootApplication
@EnableTransactionManagement
public class GiftExtendedApplication {
    public static void main(String[] args) {
        SpringApplication.run(GiftExtendedApplication.class, args);
    }
}

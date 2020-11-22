package com.epam.esm.gift_extended;

import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.epam.esm.gift_extended.genertors.GeneratedSaverService;

@SpringBootApplication
@EnableTransactionManagement
@EnableAuthorizationServer
@EnableResourceServer
public class GiftExtendedApplication {


    public static void main(String[] args) {
        SpringApplication.run(GiftExtendedApplication.class, args);
    }
}

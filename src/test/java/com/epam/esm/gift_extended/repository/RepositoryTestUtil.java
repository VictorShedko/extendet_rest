package com.epam.esm.gift_extended.repository;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.repository.forbidentouse.CertificateRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.TagRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.UserRepositoryWithSpringData;

@Service
public class RepositoryTestUtil {

    public static Certificate certificate1 = new Certificate();
    public static Certificate certificate2 = new Certificate();
    public static Certificate certificate3 = new Certificate();

    public static User user1 = new User();
    public static User user2 = new User();

    public static Tag tag1 = new Tag();
    public static Tag tag2 = new Tag();
    public static Tag tag3 = new Tag();

    static {
        certificate1.setName("t1");
        certificate1.setCreationTime(new Date());
        certificate1.setDescription("td1");
        certificate1.setPrice(25.0f);
        certificate1.setUpdateTime(new Date());

        certificate2.setName("t2");
        certificate2.setCreationTime(new Date());
        certificate2.setDescription("td2");
        certificate2.setPrice(252.0f);
        certificate2.setUpdateTime(new Date());

        certificate3.setName("t3");
        certificate3.setCreationTime(new Date());
        certificate3.setDescription("td3");
        certificate3.setPrice(253.0f);
        certificate3.setUpdateTime(new Date());

        user1.setName("user1");
        user2.setName("user2");

        tag1.setName("tag1");
        tag2.setName("tag2");
        tag3.setName("tag3");


        certificate1.setHolder(user1);
        certificate2.setHolder(user1);
        certificate3.setHolder(user2);

        certificate1.attachTag(tag1);
        certificate1.attachTag(tag2);
        certificate2.attachTag(tag2);
        certificate3.attachTag(tag3);
    }

    public static void configure(TagRepositoryWith tagRepository, UserRepositoryWithSpringData userRepository,
            CertificateRepositoryWith certificateRepository) {

        certificate1.setHolder(user1);
        certificate2.setHolder(user1);
        certificate3.setHolder(user2);

        certificateRepository.save(certificate1);
        certificateRepository.save(certificate2);
        certificateRepository.save(certificate3);
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        userRepository.save(user1);
        userRepository.save(user2);

        certificate1= certificateRepository.findByName(certificate1.getName()).get(0);
        certificate2= certificateRepository.findByName(certificate2.getName()).get(0);
        certificate3= certificateRepository.findByName(certificate3.getName()).get(0);

        tag1= tagRepository.findByName(tag1.getName()).get();
        tag2= tagRepository.findByName(tag2.getName()).get();
        tag3= tagRepository.findByName(tag3.getName()).get();

        user1=userRepository.findByName(user1.getName()).get();
        user2=userRepository.findByName(user2.getName()).get();
    }

}

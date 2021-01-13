package com.epam.esm.gift_extended.genertors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.repository.CertificateRepositoryImpl;
import com.epam.esm.gift_extended.repository.SpringDataCertificateRepository;
import com.epam.esm.gift_extended.repository.SpringDataOrderRepository;
import com.epam.esm.gift_extended.repository.SpringDataTagRepository;
import com.epam.esm.gift_extended.repository.SpringDataUserRepository;
import com.epam.esm.gift_extended.repository.TagRepository;
import com.epam.esm.gift_extended.repository.TagRepositoryImpl;
import com.epam.esm.gift_extended.repository.UserRepositoryImpl;

@Service
public class GeneratedSaverService {

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataTagRepository tagRepository;

    @Autowired
    private SpringDataCertificateRepository certificateRepository;

    @Autowired
    private SpringDataOrderRepository orderRepository;

    public void saveToRepo(List<User> users, List<Tag> tags, List<Certificate> certificates,List<Order> orders) {
        users.forEach(user -> userRepository.save(user));
        tags.forEach(tag -> tagRepository.save(tag));
        certificates.forEach(certificate -> certificateRepository.save(certificate));
        orders.forEach(order -> orderRepository.save(order));
    }

    public void generateEntities(int userAmount, int tagAmount, int certAmount) {
        UserGenerator userGenerator = new UserGenerator();
        CertificateGenerator certificateGenerator = new CertificateGenerator();
        TagGenerator tagGenerator = new TagGenerator();

        List<User> users = userGenerator.generateUserList(userAmount);
        List<Tag> tags = tagGenerator.generateTagList(tagAmount);
        List<Certificate> certificates = certificateGenerator.generateCertificateList(certAmount);

        RelationGenerator relationGenerator = new RelationGenerator();
        List<Order> orders=relationGenerator.MakeRelation(users, certificates, tags,3000);

        saveToRepo(users, tags, certificates,orders);
    }

}

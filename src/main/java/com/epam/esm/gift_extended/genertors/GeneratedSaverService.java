package com.epam.esm.gift_extended.genertors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.repository.forbidentouse.CertificateRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.TagRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.UserRepositoryWithSpringData;

@Service
public class GeneratedSaverService {

    @Autowired
    private UserRepositoryWithSpringData userRepository;

    @Autowired
    private TagRepositoryWith tagRepository;

    @Autowired
    private CertificateRepositoryWith certificateRepository;

    public void saveToRepo(List<User> users, List<Tag> tags, List<Certificate> certificates) {
        users.forEach(user -> userRepository.save(user));
        tags.forEach(tag -> tagRepository.save(tag));
        certificates.forEach(certificate -> certificateRepository.save(certificate));
    }

    public void generateEntities(int userAmount, int tagAmount, int certAmount) {
        UserGenerator userGenerator = new UserGenerator();
        CertificateGenerator certificateGenerator = new CertificateGenerator();
        TagGenerator tagGenerator = new TagGenerator();

        List<User> users = userGenerator.generateUserList(userAmount);
        List<Tag> tags = tagGenerator.generateTagList(tagAmount);
        List<Certificate> certificates = certificateGenerator.generateCertificateList(certAmount);

        RelationGenerator relationGenerator = new RelationGenerator();
        relationGenerator.MakeRelation(users, certificates, tags);

        saveToRepo(users, tags, certificates);
    }

}

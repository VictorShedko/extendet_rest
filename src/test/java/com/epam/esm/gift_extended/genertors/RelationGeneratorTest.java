package com.epam.esm.gift_extended.genertors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

class RelationGeneratorTest {

    private CertificateGenerator certificateGenerator=new CertificateGenerator();
    private TagGenerator tagGenerator=new TagGenerator();
    private UserGenerator userGenerator=new UserGenerator();
    private RelationGenerator relationGenerator=new RelationGenerator();
    @Test
    void makeRelation() {
        List<User> users=userGenerator.generateUserList(100);
        List<Certificate> certificates=certificateGenerator.generateCertificateList(100);
        List<Tag> tags=tagGenerator.generateTagList(100);

        relationGenerator.MakeRelation(users,certificates,tags);

        assertNotEquals(null,certificates.get(0).getHolder());
        assertNotEquals(0,certificates.get(0).getTags().size());
    }
}
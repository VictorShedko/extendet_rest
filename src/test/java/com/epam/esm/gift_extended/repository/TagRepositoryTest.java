package com.epam.esm.gift_extended.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.repository.forbidentouse.CertificateRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.TagRepositoryWith;
import com.epam.esm.gift_extended.repository.forbidentouse.UserRepositoryWithSpringData;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableTransactionManagement
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
class TagRepositoryTest {

    @Autowired
    private CertificateRepositoryWith certificateRepository;

    @Autowired
    private TagRepositoryWith tagRepository;

    @Autowired
    private UserRepositoryWithSpringData userRepository;
    private boolean isConfigured;

    @Test
    @DatabaseSetup("1.xml")
    void findByName() {
        Tag tag = tagRepository.findByName("tag1").get();

        assertEquals(tag.getName(), "tag1");
    }

    @Transactional
    @Test
    @DatabaseSetup("1.xml")
    void add() {
        Tag tag=new Tag();
        tag.setId(100);
        tag.setName("newTag");
        tagRepository.save(tag);
        Optional<Tag> tags=tagRepository.findByName("newTag");

        assertTrue(tags.isPresent());
        Iterable<Tag> newTags=tagRepository.findAll();
        List<Tag> tagList=new ArrayList<Tag>();
        newTags.forEach(tagList::add);
        assertEquals(7,tagList.size());
    }

    @Test
    @DatabaseSetup("1.xml")
    void find() {
        Optional<Tag> tag=tagRepository.findById(1);
        assertTrue(tag.isPresent());
        assertEquals(1,tag.get().getId());
    }

    @Test
    @DatabaseSetup("1.xml")
    void delete() {
        Optional<Tag> tag=tagRepository.findById(1);
        tagRepository.delete(tag.get());
        Iterable<Tag> tags=tagRepository.findAll();
        List<Tag> tagList=new ArrayList<Tag>();
        tags.forEach(tagList::add);
        assertEquals(6,tagList.size());
    }

    @Test
    @DatabaseSetup("1.xml")
    void all() {
        Iterable<Tag> tags=tagRepository.findAll();
        List<Tag> tagList=new ArrayList<Tag>();
        tags.forEach(tagList::add);
        assertEquals(7,tagList.size());
    }

}
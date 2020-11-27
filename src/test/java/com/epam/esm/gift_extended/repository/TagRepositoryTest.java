package com.epam.esm.gift_extended.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.GiftExtendedApplication;
import com.epam.esm.gift_extended.entity.Tag;

@ExtendWith(SpringExtension.class)
@EnableTransactionManagement
@SpringBootTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DataSource dataSource;

    @Test

    void findByName() {
        Tag tag = tagRepository.findByName("tag1").get();

        assertEquals(tag.getName(), "tag1");
    }

    @Transactional
    @Test
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
        assertEquals(9,tagList.size());
    }

    @Test
    void find() {
        Optional<Tag> tag=tagRepository.findById(1);
        assertTrue(tag.isPresent());
        assertEquals(1,tag.get().getId());
    }

    @Test
    void delete() {
        Optional<Tag> tag=tagRepository.findById(13);
        tagRepository.delete(tag.get());
        Iterable<Tag> tags=tagRepository.findAll();
        List<Tag> tagList=new ArrayList<Tag>();
        tags.forEach(tagList::add);
        assertEquals(7,tagList.size());
    }

    @Test
    void all() {
        Iterable<Tag> tags=tagRepository.findAll();
        List<Tag> tagList=new ArrayList<Tag>();
        tags.forEach(tagList::add);
        assertEquals(7,tagList.size());
    }

}
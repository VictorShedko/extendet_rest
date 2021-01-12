//package com.epam.esm.gift_extended.repository;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import javax.sql.DataSource;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.epam.esm.gift_extended.entity.Tag;
//
//@ExtendWith(SpringExtension.class)
//@EnableTransactionManagement
//@SpringBootTest
//class TagRepositoryImplTest {
//
//    @Autowired
//    private TagRepositoryImpl tagRepository;
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    void autoInjectCorrectly() {
//        assertNotNull(tagRepository);
//        assertNotNull(dataSource);
//    }
//
//    @Test
//    void findByName() {
//        Optional<Tag> tag = tagRepository.findByName("spa");
//        assertEquals(9,tag.get().getId());
//
//        Optional<Tag> tag2 = tagRepository.findByName("none");
//        assertFalse(tag2.isPresent());
//    }
//
//    @Test
//    void findAll() {
//        Iterable<Tag> tags=tagRepository.findAll();
//        List<Tag> tagList=new ArrayList<Tag>();
//        tags.forEach(tagList::add);
//        assertEquals(7,tagList.size());
//    }
//
//    @Test
//    void save() {
//        Tag tag=new Tag();
//        tag.setName("newTag");
//        tagRepository.save(tag);
//        Optional<Tag> tags=tagRepository.findByName("newTag");
//
//        assertTrue(tags.isPresent());
//        Iterable<Tag> newTags=tagRepository.findAll();
//        List<Tag> tagList=new ArrayList<Tag>();
//        newTags.forEach(tagList::add);
//        assertEquals(8,tagList.size());
//        tagRepository.delete(tag);
//        Iterable<Tag> tagss=tagRepository.findAll();
//
//    }
//
//    @Test
//    void findById() {
//        Optional<Tag> tag = tagRepository.findById(10);
//        assertEquals(10,tag.get().getId());
//
//        Optional<Tag> tag2 = tagRepository.findById(100);
//        assertFalse(tag2.isPresent());
//    }
//
//
//    @Test
//    void count() {
//        long count=tagRepository.count();
//        assertEquals(7,count);
//    }
//
//    @Test
//    void delete() {
//        Optional<Tag> tag=tagRepository.findById(13);
//        tagRepository.delete(tag.get());
//        Iterable<Tag> tags=tagRepository.findAll();
//        List<Tag> tagList=new ArrayList<Tag>();
//        tags.forEach(tagList::add);
//        assertEquals(6,tagList.size());
//        tagRepository.save(tag.get());
//    }
//
//    @Test
//    void isExist() {
//        Tag existTag=new Tag();
//        existTag.setName("spa");
//        boolean answer1=tagRepository.isExist(existTag);
//        assertTrue(answer1);
//
//        Tag notExistTag=new Tag();
//        existTag.setName("not existing name");
//        boolean answer2=tagRepository.isExist(notExistTag);
//        assertFalse(answer2);
//
//    }
//}
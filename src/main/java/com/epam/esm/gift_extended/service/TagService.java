package com.epam.esm.gift_extended.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.exception.GiftException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.TagRepository;

@Service
public class TagService implements GiftService<Tag> {

    private TagRepository repository;

    @Autowired
    private CertificateService certificateService;


    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public Iterable<Tag> all() {
        return repository.findAll();
    }

    @Override
    public List<Tag> allWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return repository.findAll(pageable);
    }

    @Override
    public Tag findById(Integer tagId) {

        return repository.findById(tagId).orElseThrow(()->new ResourceNotFoundedException("Tag",tagId.toString()));
    }


    public void add(String tagName) {
        Tag newTag=new Tag();
        newTag.setName(tagName);
        save(newTag);
    }

    @Override
    public void save(Tag tag){
        repository.save(tag);
    }

    @Override
    public void delete(Integer tagId) {
        Optional<Tag> tagToDelete=repository.findById(tagId);
        tagToDelete.ifPresent(tag -> repository.delete(tag));
    }

    public Iterable<Tag> tags(int certId) {

        return certificateService.findById(certId).getTags();
    }

    public Optional<Tag> findByName(String name){
        return repository.findByName(name);
    }

    public Tag findMostPopular(){
        return repository.findMostPopularTagFromRichestUserBySumOfCertificatePrice().orElseThrow(GiftException::new);
    }

    @Override
    public long countEntities(){
        return repository.count();
    }

    @Override
    public boolean isExist(Tag t) {
        return repository.isExist(t);
    }

}

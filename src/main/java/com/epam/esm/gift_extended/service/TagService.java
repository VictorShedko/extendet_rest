package com.epam.esm.gift_extended.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.exception.GiftException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.TagRepository;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class TagService implements GiftService<Tag> {

    private final TagRepository repository;

    @Autowired
    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public Iterable<Tag> all() {
        return repository.findAll();
    }

    @Override
    public List<Tag> allWithPagination(int page, int size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findAll(pageable);
    }

    @Override
    public Tag findById(Integer tagId) {

        return repository.findById(tagId).orElseThrow(() -> new ResourceNotFoundedException("Tag", tagId.toString()));
    }

    public void saveFromString(String tagName) {
        Tag newTag = new Tag();
        newTag.setName(tagName);
        save(newTag);
    }

    @Override
    public void save(Tag tag) {
        repository.save(tag);
    }

    @Override
    public void delete(Integer tagId) {
        Optional<Tag> tagToDelete = repository.findById(tagId);
        tagToDelete.ifPresent(repository::delete);
    }

    public List<Tag> tags(int certId, Integer page, Integer size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findByCert(certId,pageable);
    }

    public Optional<Tag> findByName(String name) {
        return repository.findByName(name);
    }

    public Tag findMostPopular() {
        return repository.findMostPopularTagFromRichestUserBySumOfCertificatePrice().orElseThrow(GiftException::new);
    }

    @Override
    public long countEntities() {
        return repository.count();
    }

    @Override
    public boolean isExist(Tag t) {
        return repository.isExist(t);
    }

}

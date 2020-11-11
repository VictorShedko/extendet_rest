package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

public class CertificateRepositoryImpl implements CertificateRepository {
    private EntityManager manager;

    @Autowired
    public CertificateRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Certificate> findUserCertificates(Integer userId) {
        return null;
    }

    @Override
    public List<Certificate> findDistinctByTags(Tag tag) {
        return null;
    }

    @Override
    public List<Certificate> findByContainsAllTagNames(List<Tag> tags) {
        return null;
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        return null;
    }

    @Override
    public List<Certificate> findCertificateByHolderAndTags(User holder, Tag tag) {
        return null;
    }

    @Override
    public List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String name, String description) {
        return null;
    }


    @Override
    public Page<Certificate> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Certificate> S save(S s) {
        return null;
    }


    @Override
    public Optional<Certificate> findById(Integer integer) {
        return Optional.empty();
    }


    @Override
    public List<Certificate> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Certificate certificate) {

    }


}

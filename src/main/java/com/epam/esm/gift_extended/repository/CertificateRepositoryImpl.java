package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    private EntityManager manager;

    @Autowired
    public CertificateRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Certificate> findUserCertificates(Integer userId) {
        Query query=manager.createQuery("SELECT cert "
                + "FROM Certificate cert "
                + "WHERE cert.holder.id = :id ");
        query.setParameter("id",userId);
        return query.getResultList();
    }


    @Override
    public List<Certificate> findByContainsAllTagNames(List<Tag> tags) {
        Query query=manager.createQuery("SELECT DISTINCT cert "
                + "FROM Certificate cert "
                + "join cert.tags as tag "
                + "WHERE tag in :tags "
                + "group by cert having count(tag)=:tagSize");
        query.setParameter("tags",tags);
        query.setParameter("tagSize",tags.size());
        return query.getResultList();
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        Query query=manager.createQuery("SELECT C FROM Certificate as C WHERE C.name=:name");
        query.setParameter("name",name);
        return Optional.ofNullable((Certificate) query.getSingleResult());
    }

    @Override
    public List<Certificate> findCertificateByHolderAndTags(User holder, Tag tag) {
        Query query=manager.createQuery("SELECT DISTINCT C FROM Certificate as C join Tag as T WHERE C.holder=:user and T=:tag");
        query.setParameter("user",holder);
        query.setParameter("tag",tag);
        return query.getResultList();
    }

    @Override
    public List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String name, String description) {
        Query query=manager.createQuery("SELECT c FROM Certificate c "
                + "WHERE c.name like CONCAT('%',:name,'%') or c.description like CONCAT('%',:description,'%')");
        query.setParameter("name",name);
        query.setParameter("description",description);
        return query.getResultList();
    }


    @Override
    public List<Certificate> findAll(Pageable pageable) {
        Query query=manager.createQuery("SELECT C FROM Certificate as C ");
        query.setFirstResult(pageable.getPageSize()*pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public <S extends Certificate> S save(S s) {
        if (s.getId() == null) {
            manager.persist(s);
        } else {
            s = manager.merge(s);
        }
        return s;
    }


    @Override
    public Optional<Certificate> findById(Integer integer) {
        return Optional.ofNullable( manager.find(Certificate.class,integer));
    }


    @Override
    public List<Certificate> findAll() {
        Query query=manager.createQuery("SELECT C FROM Certificate as C ");
        return query.getResultList();
    }

    @Override
    public long count() {
        Query query=manager.createQuery("SELECT count(C) FROM Certificate as C ");
        return (Long) query.getSingleResult();
    }

    @Override
    public void delete(Certificate certificate) {
        if(manager.contains(certificate)){
            manager.remove(certificate);
        }
    }


}

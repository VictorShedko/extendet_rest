package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.UniqFieldException;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private EntityManager manager;

    @Autowired
    public CertificateRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Certificate> findUserCertificates(Integer userId) {
        Query query = manager.createQuery("SELECT cert " + "FROM Certificate cert " + "WHERE cert.holder.id = :id ");
        query.setParameter("id", userId);
        return query.getResultList();
    }

    @Override
    public List<Certificate> findByContainsAllTagNames(List<Tag> tags) {
        Query query = manager.createQuery(
                "SELECT DISTINCT cert " + "FROM Certificate cert " + "join cert.tags as tag " + "WHERE tag in :tags "
                        + "group by cert having count(tag)=:tagSize");
        query.setParameter("tags", tags);
        query.setParameter("tagSize", (long) tags.size());
        return query.getResultList();
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        Query query = manager.createQuery("SELECT C FROM Certificate as C WHERE C.name=:name");
        query.setParameter("name", name);
        try {
            return Optional.ofNullable((Certificate) query.getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Certificate> findCertificateByHolderAndTag(User holder, Tag tag) {
        Query query = manager.createQuery(
                "SELECT C FROM Certificate as C join C.tags as T WHERE T=:tag and C.holder=:user");
        query.setParameter("user", holder);
        query.setParameter("tag", tag);
        return query.getResultList();
    }

    @Override
    public List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String name, String description) {
        Query query = manager.createQuery("SELECT c FROM Certificate c "
                + "WHERE c.name like CONCAT('%',:name,'%') or c.description like CONCAT('%',:description,'%')");
        query.setParameter("name", name);
        query.setParameter("description", description);
        return query.getResultList();
    }

    @Override
    public List<Certificate> findAll(Pageable pageable) {
        Query query = manager.createQuery("SELECT C FROM Certificate as C ");
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Transactional
    @Override
    public <S extends Certificate> S save(S s) {
        try {
            if (s.getId() == null) {
                manager.persist(s);
            } else {
                s = manager.merge(s);
            }
        } catch (ConstraintViolationException ex) {
            throw new UniqFieldException("name");
        }
        return s;

    }

    @Override
    public Optional<Certificate> findById(Integer integer) {
        return Optional.ofNullable(manager.find(Certificate.class, integer));
    }

    @Override
    public List<Certificate> findAll() {
        Query query = manager.createQuery("SELECT C FROM Certificate as C ");
        return query.getResultList();
    }

    @Override
    public long count() {
        Query query = manager.createQuery("SELECT count(C) FROM Certificate as C ");
        return (Long) query.getSingleResult();
    }

    @Transactional
    @Override
    public void delete(Certificate certificate) {

        Certificate certToDelete = manager.find(certificate.getClass(), certificate.getId());
        if (certToDelete != null) {

            manager.remove(certToDelete);
            manager.flush();
            manager.clear();

        }
    }

    @Override
    public boolean isExist(Certificate certificate) {
        Query query = manager.createQuery("SELECT COUNT(c) FROM Certificate c WHERE c.id = :cert");
        query.setParameter("cert", certificate.getId());
        return (Long) query.getSingleResult() > 0;
    }

}

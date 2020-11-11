package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.epam.esm.gift_extended.entity.Tag;

public class TagRepositoryImpl implements TagRepository{
    EntityManager manager;

    @Autowired
    public TagRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Query query=manager.createQuery("SELECT T FROM Tag as T WHERE T.name=:name");
        query.setParameter("name",name);
        return Optional.ofNullable((Tag) query.getSingleResult());
    }

    @Override
    public Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice() {
        Query query=manager.createNativeQuery("SELECT t.id,t.name "
                + "FROM gift_ex.user as u "
                + "    JOIN gift_ex.certificate as c on c.user_id=u.user_id"
                + "    JOIN gift_ex.certificate_tags as ct on ct.certificate_id=c.id"
                + "    JOIN gift_ex.tag as t on t.id=ct.tags_id "
                + "WHERE c.user_id=(SELECT gift_ex.user.user_id "
                + "                 FROM gift_ex.user "
                + "           "
                + "          JOIN gift_ex.certificate as c on user.user_id = c.user_id "
                + "                 GROUP BY c.user_id "
                + "                     ORDER BY SUM(c.price) DESC "
                + "                 LIMIT 1) "
                + "    GROUP BY t.id "
                + "    ORDER BY COUNT(t.id) DESC "
                + "    LIMIT 1");
        return Optional.ofNullable((Tag)query.getSingleResult());
    }


    @Override
    public Page<Tag> findAll(Pageable pageable) {
        Query query=manager.createQuery("SELECT T FROM Tag as T ");
        query.setFirstResult(pageable.getPageSize()*pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        return null;
    }

    @Override
    public <S extends Tag> S save(S s) {
        if (s.getId() == null) {
            manager.persist(s);
        } else {
            s = manager.merge(s);
        }
        return s;
    }


    @Override
    public Optional<Tag> findById(Integer integer) {
       return Optional.ofNullable( manager.find(Tag.class,integer));

    }

    @Override
    public List<Tag> findAll() {
        Query query=manager.createQuery("SELECT T FROM Tag as T ");
        return query.getResultList();
    }



    @Override
    public long count() {
        Query query=manager.createQuery("SELECT count (T) FROM Tag as T ");
        return (Long)query.getSingleResult();
    }

    @Override
    public void delete(Tag tag) {
        if(manager.contains(tag)){
            manager.remove(tag);
        }
    }

}
package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.exception.UniqFieldException;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Repository
public class TagRepositoryImpl implements TagRepository {

    EntityManager manager;

    @Autowired
    public TagRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Query query = manager.createQuery("SELECT T FROM Tag as T WHERE T.name=:name");
        query.setParameter("name", name);
        try {
            return Optional.ofNullable((Tag) query.getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice() {
        Query query = manager.createNativeQuery("SELECT t.id,t.name " + ""
                + "    FROM user as u "
                + "    JOIN user_order on u.user_id=user_order.customer_user_id"
                + "    JOIN user_order_certificates uoc on user_order.id = uoc.order_id "
                + "    JOIN certificate as c on c.id=uoc.certificates_id"
                + "    JOIN certificate_tags as ct on ct.certificate_id=c.id"
                + "    JOIN tag as t on t.id=ct.tags_id " + "WHERE c.user_id="
                + "(SELECT user.user_id,user.name " + "                     FROM user " + "           "
                + "                         JOIN user_order as o on user.user_id = o.customer_user_id "
                + "                 GROUP BY user.user_id " + "                 ORDER BY SUM(o.order_cost) DESC "
                + "                 LIMIT 1 )"

                + "GROUP BY t.id " + "    " + "ORDER BY COUNT(t.id) DESC " + "LIMIT 1", Tag.class);
        return Optional.ofNullable((Tag) query.getSingleResult());
    }

    @Override
    public List<Tag> findAll(PageSortInfo pageable) {
        Query query = manager.createQuery(
                "SELECT T FROM Tag as T order by T.name "); //+ pageable.getSortDirection().getTypeAsString());
      //  query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
       // query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    @Transactional
    public <S extends Tag> S save(S s) {
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
    public Optional<Tag> findById(Integer integer) {
        try {
            return Optional.ofNullable(manager.find(Tag.class, integer));
        } catch (NoResultException exception) {
            return Optional.empty();
        }

    }

    @Override
    public List<Tag> findAll() {
        Query query = manager.createQuery("SELECT T FROM Tag as T ");
        return query.getResultList();
    }

    public List<Tag> findByCert(int certId) {
        Query query = manager.createQuery(
                "SELECT T FROM Certificate C inner join C.tags T where C.id=:certId order by T.name");
        query.setParameter("certId", certId);
        return query.getResultList();
    }

    @Override
    public List<Tag> findByCert(int certId, PageSortInfo pageable) {
        Query query = RepositoryUtil.addPaginationToQuery(manager, pageable,
                "SELECT T FROM Certificate C inner join C.tags T where C.id=:certId order by T.name");
        query.setParameter("certId", certId);
        return query.getResultList();
    }

    @Override
    public long count() {
        Query query = manager.createQuery("SELECT count (T) FROM Tag as T ");
        return (Long) query.getSingleResult();
    }

    @Transactional
    @Override
    public void delete(Tag tag) {
        Tag tagToDelete = manager.find(tag.getClass(), tag.getId());
        if (tagToDelete != null) {
            manager.remove(tagToDelete);
            manager.flush();
            manager.clear();
        }
    }

    @Override
    public boolean isExist(Tag tag) {
        Query query = manager.createQuery("SELECT COUNT(t) FROM Tag t WHERE t.name = :tag");
        query.setParameter("tag", tag.getName());
        return (Long) query.getSingleResult() > 0;
    }

}

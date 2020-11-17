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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.UniqFieldException;

@Repository
public class UserRepositoryImpl implements UserRepository {
    EntityManager manager;

    @Autowired
    public UserRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<User> findRichestByOrderPriceSum() {
        Query query = manager.createNativeQuery("SELECT user.user_id,user.name "
                + "                     FROM user " + "           "
                + "                         JOIN certificate as c on user.user_id = c.user_id "
                + "                 GROUP BY c.user_id " + "                     ORDER BY SUM(c.price) DESC "
                + "                 LIMIT 1 ",User.class);
        try {
            List<Object> result=query.getResultList();
            return Optional.ofNullable((User) query.getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        Query query=manager.createQuery("SELECT U FROM User as U WHERE U.name=:name");
        query.setParameter("name",name);
        try {
            return Optional.ofNullable((User) query.getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findByNameContains(String partOfName) {
        Query query=manager.createQuery("SELECT U FROM User as U WHERE U.name like :name");
        query.setParameter("name","%"+partOfName+"%");
        return query.getResultList();
    }


    @Override
    public List<User> findAll(Pageable pageable) {
        Query query=manager.createQuery("SELECT U FROM User as U ");
        query.setFirstResult(pageable.getPageSize()*pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Transactional
    @Override
    public <S extends User> S save(S s) {
        try {


        if (s.getId() == null) {
            manager.persist(s);
        } else {
            s = manager.merge(s);
        }
        }catch (ConstraintViolationException ex){
            throw new UniqFieldException("name");
        }
        return s;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        try {
            return Optional.ofNullable(manager.find(User.class,integer));
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {

        Query query=manager.createQuery("SELECT U FROM User as U ");
        return query.getResultList();
    }

    @Override
    public long count() {
        Query query=manager.createQuery("SELECT count (U) FROM User as U ");
        return (Long)query.getSingleResult();
    }

    @Transactional
    @Override
    public void delete(User user) {
        User userToDelete = manager.find(user.getClass(), user.getId());
        if (userToDelete!=null) {
            manager.remove(userToDelete);
            manager.flush();
            manager.clear();

        }
    }

    @Override
    public boolean isExist(User user) {
        Query query=manager.createQuery("SELECT COUNT(u) FROM User u WHERE u.name = :user");
        query.setParameter("user",user.getName());
        return (Long)query.getSingleResult()>0;
    }

}

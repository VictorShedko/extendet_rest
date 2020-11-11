package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

public class UserRepositoryImpl implements UserRepository {
    EntityManager manager;

    @Autowired
    public UserRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<User> findRichestByOrderPriceSum() {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        Query query=manager.createQuery("SELECT U FROM User as U WHERE U.name=:name");
        query.setParameter("name",name);
        return Optional.ofNullable((User) query.getSingleResult());
    }

    @Override
    public List<User> findByNameContains(String partOfName) {
        Query query=manager.createQuery("SELECT T FROM Tag as T WHERE T.name like :name");
        query.setParameter("name","%"+partOfName+"%");
        return query.getResultList();
    }


    @Override
    public Page<User> findAll(Pageable pageable) {
        Query query=manager.createQuery("SELECT T FROM Tag as T ");
        query.setFirstResult(pageable.getPageSize()*pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        return null;
    }

    @Override
    public <S extends User> S save(S s) {

        if (s.getId() == null) {
            manager.persist(s);
        } else {
            s = manager.merge(s);
        }
        return s;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        return Optional.ofNullable(manager.find(User.class,integer));
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

    @Override
    public void delete(User user) {
        if(manager.contains(user)){
            manager.remove(user);
        }
    }

}

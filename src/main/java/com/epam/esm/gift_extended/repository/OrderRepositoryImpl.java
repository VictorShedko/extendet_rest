package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.exception.UniqFieldException;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private EntityManager manager;

    @Autowired
    public OrderRepositoryImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Order> findAll(PageSortInfo pageable) {
        Query query = RepositoryUtil.addPaginationToQuery(manager, pageable,
                "SELECT O FROM Order as O order by O.orderDate ");
        return query.getResultList();
    }

    @Override
    public List<Order> findAll() {
        Query query = manager.createQuery("SELECT O FROM Order as O order by O.orderDate");
        return query.getResultList();
    }

    @Transactional
    @Override
    public <S extends Order> S save(S s) {
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
    public Optional<Order> findById(Integer integer) {
        try {
            return Optional.ofNullable(manager.find(Order.class, integer));
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    @Override
    public long count() {
        Query query = manager.createQuery("SELECT count (O) FROM Order as O ");
        return (Long) query.getSingleResult();
    }

    @Override
    public void delete(Order ent) {
        Order userToDelete = manager.find(ent.getClass(), ent.getId());
        if (userToDelete != null) {
            manager.remove(userToDelete);
            manager.flush();
            manager.clear();

        }
    }

    @Override
    public boolean isExist(Order order) {
        Query query = manager.createQuery("SELECT COUNT(O) FROM Order O WHERE O = :order");
        query.setParameter("order", order);
        return (Long) query.getSingleResult() > 0;
    }

    @Override
    public List<Order> findByUserId(Integer userId, PageSortInfo pageable) {
        Query query = RepositoryUtil.addPaginationToQuery(manager, pageable,
                "SELECT O FROM Order as O WHERE O.customer.id=:userId order by O.orderDate");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Order> findByUserId(Integer userId) {
        Query query = manager.createQuery("SELECT O FROM Order as O WHERE O.customer.id=:userId order by O.orderDate");
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}

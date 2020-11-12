package com.epam.esm.gift_extended.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface GiftService<T> {

    List<T> allWithPagination(int from, int amount);

    T findById(Integer id);

    void save(T t);

    @Transactional
    void delete(Integer id);

    Iterable<T> all();

    long countEntities();

    default long pages(long size){
        return countEntities()+1/size;
    }

    boolean isExist(T t);
}

package com.epam.esm.gift_extended.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


public interface GiftService<T> {

    Page<T> allWithPagination(int from, int amount);

    T findById(Integer id);

    void save(T t);

    @Transactional
    void delete(Integer id);

    Iterable<T> all();
}

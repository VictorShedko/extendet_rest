package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import com.epam.esm.gift_extended.service.util.PageSortInfo;

public interface GiftRepository<T> {

    Optional<T> findByName(String name);

    List<T> findAll(PageSortInfo pageable);

    <S extends T> S save(S s);

    Optional<T> findById(Integer integer);

    List<T> findAll();

    long count();

    void delete(T ent);

    boolean isExist(T t);
}

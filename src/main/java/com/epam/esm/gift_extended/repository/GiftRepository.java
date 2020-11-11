package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftRepository<T> {

    Optional<T> findByName(String name);

    Page<T> findAll(Pageable pageable);

    <S extends T> S save(S s);

    Optional<T> findById(Integer integer);

    List<T> findAll();

    long count();

    void delete(T ent);
}

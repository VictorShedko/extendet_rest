package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

public interface UserRepository extends GiftRepository<User> {

    Optional<User> findRichestByOrderPriceSum();

    List<User> findByNameContains(String partOfName);

    List<User> findByNameContains(String pattern, PageSortInfo pageable);
}
